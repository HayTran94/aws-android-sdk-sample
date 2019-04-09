package abc.abc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.results.Tokens;
import com.abc.abc.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    public void signOut(View view) {
        AWSMobileClient.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
        finish();
    }


    public void getToken(View view) {
        try {
            AWSMobileClient.getInstance().getTokens(new Callback<Tokens>() {
                @Override
                public void onResult(Tokens result) {
                    Log.d(TAG, "onClick: token: " + result.getAccessToken().getTokenString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "onError: " + e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getIdentity(View view) {
        Log.d(TAG, "getIdentity: " + AWSMobileClient.getInstance().getIdentityId());
    }
}
