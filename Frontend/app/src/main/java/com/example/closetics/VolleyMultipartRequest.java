package com.example.closetics;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> mListener;
    private final Map<String, String> headers;
    private final Map<String, DataPart> files;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.headers = new HashMap<>();
        this.files = new HashMap<>();
    }

    public void addFile(String paramName, DataPart dataPart) {
        files.put(paramName, dataPart);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }


    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    private final String boundary = "apiclient-" + System.currentTimeMillis();

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            for (Map.Entry<String, DataPart> entry : files.entrySet()) {
                buildPart(bos, entry.getKey(), entry.getValue());
            }
            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bos.toByteArray();
    }

    private void buildPart(OutputStream out, String name, DataPart dataPart) throws IOException {
        String disposition = "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + dataPart.getFileName() + "\"\r\n";
        String type = "Content-Type: " + dataPart.getType() + "\r\n\r\n";

        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(disposition.getBytes());
        out.write(type.getBytes());
        out.write(dataPart.getContent());
        out.write("\r\n".getBytes());
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
