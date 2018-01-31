package com.adiaz.deportelocal.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adiaz.deportelocal.BuildConfig;
import com.adiaz.deportelocal.R;
import com.adiaz.deportelocal.utilities.PreferencesUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_email)
    TextView tvEmail;

    @BindView(R.id.tv_validate_email)
    TextView tvValidateMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.action_user_management));
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
            // Create and launch sign-in intent
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.logo_dark)
                    .setIsSmartLockEnabled(!BuildConfig.DEBUG , true)
                    .build(),
                RC_SIGN_IN);
        } else {
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);
            tvName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());
            if (user.isEmailVerified()) {
                tvValidateMail.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (!user.isEmailVerified()) {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.user_sent_mail), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                // Sign in failed, check response for error code
                Log.d(TAG, "onActivityResult: error " + resultCode);
                startActivity(new Intent(this, SportsActivity.class));
                finish();
            }
        }
    }

    public void closeSession(View view) {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, getString(R.string.user_msg_session_closed), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, SportsActivity.class));
                        finish();
                    }
                }
            });
    }

    public void closeSessionAndDeleteUser(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getString(R.string.user_msg_user_deleted), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, SportsActivity.class));
                            finish();
                        }
                    }
                });
    }
}
