package sentinelgroup.io.sentinel.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import sentinelgroup.io.sentinel.R;
import sentinelgroup.io.sentinel.ui.dialog.SingleActionDialogFragment;
import sentinelgroup.io.sentinel.ui.fragment.ReceiveFragment;
import sentinelgroup.io.sentinel.util.AppConstants;
import sentinelgroup.io.sentinel.util.AppPreferences;

public class ReceiveActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, ReceiveFragment.OnFragmentInteractionListener {
    private Toolbar mToolbar;
    private SwitchCompat mSwitchNet;
    private TextView mToolbarTitle, mSwitchState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        initView();
        loadFragment(ReceiveFragment.newInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setup testnet switch
        setupTestNetSwitch();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        mSwitchNet = findViewById(R.id.switch_net);
        mSwitchState = findViewById(R.id.switch_state);
        //setup toolbar
        setupToolbar();
        // set click listeners
        mSwitchNet.setOnCheckedChangeListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setToolbarTitle(String iTitle) {
        mToolbarTitle.setText(iTitle);
    }

    private void showError(String iError) {
        SingleActionDialogFragment.newInstance(getString(R.string.please_note), iError, getString(android.R.string.ok))
                .show(getSupportFragmentManager(), "alert_dialog");
    }

    private void setupTestNetSwitch() {
        boolean isActive = AppPreferences.getInstance().getBoolean(AppConstants.PREFS_IS_TEST_NET_ACTIVE);
        mSwitchNet.setChecked(isActive);
        mSwitchState.setText(getString(R.string.test_net_state, getString(isActive ? R.string.active : R.string.deactive)));
    }

    private void loadFragment(Fragment iFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, iFragment).commit();
    }

    private void copyAddressToClipboard(String iAccountAddress) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(getString(R.string.app_name), iAccountAddress);
            Toast.makeText(this, R.string.address_copied, Toast.LENGTH_SHORT).show();
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        AppPreferences.getInstance().saveBoolean(AppConstants.PREFS_IS_TEST_NET_ACTIVE, isChecked);
        mSwitchState.setText(getString(R.string.test_net_state, getString(isChecked ? R.string.active : R.string.deactive)));
    }

    @Override
    public void onCopyAddressClicked(String iAccountAddress) {
        copyAddressToClipboard(iAccountAddress);
    }

    @Override
    public void onFragmentLoaded(String iTitle) {
        setToolbarTitle(iTitle);
    }

    @Override
    public void onShowErrorDialog(String iError) {
        showError(iError);
    }
}
