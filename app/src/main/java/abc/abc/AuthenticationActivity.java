package abc.abc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.HostedUIOptions;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;
import com.abc.abc.R;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AuthenticationActivity";
    private Button btnGoogle, btnFacebook, btnSignOut;
    private Button btnGetToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_social_login);

        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        btnGetToken = (Button) findViewById(R.id.btnGetToken);

        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnGetToken.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent activityIntent = getIntent();

        if (activityIntent.getData() != null) {
            Log.d(TAG, "onResume: uri" + activityIntent.getData().toString());
        }
        if (activityIntent.getData() != null &&
                "myapp".equals(activityIntent.getData().getScheme())) {
            AWSMobileClient.getInstance().handleAuthResponse(activityIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGoogle:
                loginGoogle();
                break;
            case R.id.btnFacebook:
                loginFaceBook();
                break;
            case R.id.btnSignOut:
                break;
            case R.id.btnGetToken:
                loginDefault();
                break;
        }
    }

    private void loginFaceBook() {
        // For Facebook
        HostedUIOptions hostedUIOptions = HostedUIOptions.builder()
                .scopes("openid", "email")
                .identityProvider("Facebook")
                .build();
        signIn(hostedUIOptions);
    }

    private void loginGoogle() {
        // For Google
        HostedUIOptions hostedUIOptions = HostedUIOptions.builder()
                .scopes("openid", "email")
                .identityProvider("Google")
                .build();
        signIn(hostedUIOptions);
    }


    private void loginDefault() {
        HostedUIOptions hostedUIOptions = HostedUIOptions.builder()
                .scopes("openid", "email")
                .build();
        signIn(hostedUIOptions);
        new Exception().printStackTrace();
    }

    void signIn(HostedUIOptions hostedUIOptions) {
        SignInUIOptions signInUIOptions = SignInUIOptions.builder()
                .hostedUIOptions(hostedUIOptions)
                .build();
        AWSMobileClient.getInstance().showSignIn(this, signInUIOptions, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails details) {
                Log.d(TAG, "onResult: " + details.getUserState());
                Log.d(TAG, "onResult: IdentityId " + AWSMobileClient.getInstance().getIdentityId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

}
