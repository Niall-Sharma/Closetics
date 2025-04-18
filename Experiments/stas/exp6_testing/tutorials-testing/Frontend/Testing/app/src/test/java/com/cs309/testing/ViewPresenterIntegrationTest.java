package com.cs309.testing;

import android.content.Context;
import android.os.Build;
import android.widget.EditText;

import com.cs309.testing.Model.RequestServerForService;
import com.cs309.testing.Presenter.MainActivityPresenter;
import com.cs309.testing.View.MainActivity;
import com.cs309.testing.View.TestingView;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
//@Config(sdk = Build.VERSION_CODES.O_MR1)
public class ViewPresenterIntegrationTest {

    @Mock
    private RequestServerForService server;

    @Before
    public void setUpMockito() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {

        // test
        MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();

        MainActivityPresenter presenter = activity.test_getPresenter();
        presenter.setModel(server);

        ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);

        doNothing().when(server).reverse(argumentCaptor1.capture());
        doNothing().when(server).capitalize(argumentCaptor2.capture());


        // enter text
        EditText stringEntry = activity.findViewById(R.id.stringEntry);
        stringEntry.setText("hello");

        // click the continue button
        activity.findViewById(R.id.submit).callOnClick();

        // check if correct request server method was called
        //verify(server).capitalize(argumentCaptor2.capture());   // THIS WILL FAIL
        //assertEquals("hello", argumentCaptor2.getValue());
        verify(server).reverse(argumentCaptor1.capture());
        assertEquals("hello", argumentCaptor1.getValue());


        // check if correct request server method was called
        activity.findViewById(R.id.submit2).callOnClick();
        verify(server).capitalize(argumentCaptor2.capture());
        assertEquals("hello", argumentCaptor2.getValue());


    }

}
