package com.cs309.testing;

import android.content.Context;

import com.cs309.testing.Model.RequestServerForService;
import com.cs309.testing.Presenter.MainActivityPresenter;
import com.cs309.testing.View.TestingView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PresenterUnitTest {


    @Mock
    private Context context;

    @Mock
    private TestingView view;

    @Mock
    private RequestServerForService stringServer;

    @Test
    public void whenRevStr_thenReverseString(){
        // want to call revStr and then verify showProgressBar and  call reverse methods are called.

        // Random string to update model
        String testString = "hello";

        // Initialize presenter and inject mocks
        MainActivityPresenter presenter = new MainActivityPresenter(view, context);
        presenter.setModel(stringServer); // injecting mock server

        // Test method
        presenter.revStr(testString);

        // Verify it worked
        verify(view, times(1)).showProgressBar();
        verify(stringServer,times(1)).reverse(testString);
    }

    @Test
    public void whenCapStr_thenCapitalizeString(){

        // want to call capStr and then verify showProgressBar and  call capitalize methods are called.
        // Random string to update model
        String testString = "hello";

        // Initialize presenter and inject mocks
        MainActivityPresenter presenter = new MainActivityPresenter(view, context);
        presenter.setModel(stringServer); // injecting mock server

        // Test method
        presenter.capStr(testString);

        // Verify it worked
        verify(view, times(1)).showProgressBar();
        verify(stringServer,times(1)).capitalize(testString);
    }
}
