package com.nmckinley.beveryman;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /** Fragment managing the behaviors, interactions and presentation of the navigation drawer. */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /** Used to store the last screen title. For use in {@link #restoreActionBar()}. */
    private CharSequence mTitle;

    private static final int REQUEST_ENABLE_BT = 1;

    private static final String TAG = "bluetooth1";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private static OutputStream arduinoStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    private static String address = "20:14:12:03:22:49";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle(); //defaults to app name

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        ensureBluetoothAvailable();
    }

    private void ensureBluetoothAvailable() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            exitWithErrorMessage(this, "Fatal Error", "Bluetooth not supported");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth is on...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod(
                        "createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "...onResume - try connect...");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            exitWithErrorMessage(this, "Fatal Error", "In onResume() and socket create failed: "
                    + e1.getMessage() + ".");
        }

        // Discovery is resource intensive, ensure it isn't happening.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect to socket: " + e.getMessage());
            try {
                btSocket.close();
            } catch (IOException e2) {
                exitWithErrorMessage(this, "Fatal Error", "In onResume() and unable to close " +
                        "socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");
        try {
            arduinoStream = btSocket.getOutputStream();
        } catch (IOException e) {
            exitWithErrorMessage(this, "Fatal Error", "In onResume() and output stream creation " +
                    "failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "...In onPause()...");

        if (arduinoStream != null) {
            try {
                arduinoStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "onPause() failed to flush output stream: "+ e.getMessage() + ".");
                return;
            }
        }

        try {
            btSocket.close();
        } catch (IOException e2) {
            exitWithErrorMessage(this, "Fatal Error", "onPause() failed to close socket."
                    + e2.getMessage() + ".");
        }
    }

    private static void exitWithErrorMessage(Activity activity, String title, String message){
        Log.e(TAG, title + " : " + message);
        Toast.makeText(activity.getBaseContext(), title + " - " + message,
                Toast.LENGTH_LONG).show();
        activity.finish();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title1);
                break;
            case 2:
                mTitle = getString(R.string.title2);
                break;
            case 3:
                mTitle = getString(R.string.title3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected static void sendDataToBeveryman(Activity activity, String message) {
        if (arduinoStream == null) {
            Toast.makeText(activity.getBaseContext(), "Beveryman not connected.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] msgBuffer = message.getBytes();
        Log.d(TAG, "...Send data: " + message + "...");

        try {
            arduinoStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In sendDataToBeveryman() and an exception occurred during write: "
                    + e.getMessage();
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on " +
                    "server.\n\n";
            exitWithErrorMessage(activity, "Fatal Error", msg);
        }
    }

    /** A placeholder fragment containing a simple view. */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int section;

        /** Returns a new instance of this fragment for the given section number. */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            section = sectionNumber;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            updateDrinkView(rootView);
            return rootView;
        }

        private void updateDrinkView(View rootView) {
            TextView mDrinkTitle = (TextView) rootView.findViewById(R.id.drink_title);
            TextView mDrinkDescr = (TextView) rootView.findViewById(R.id.drink_description);
            TextView mDrinkIngre = (TextView) rootView.findViewById(R.id.drink_ingredients);

            switch (section) {
                case 1:
                    mDrinkTitle.setText(getString(R.string.title1));
                    mDrinkDescr.setText(getString(R.string.description1));
                    mDrinkIngre.setText(getString(R.string.ingredients1));
                    break;
                case 2:
                    mDrinkTitle.setText(getString(R.string.title2));
                    mDrinkDescr.setText(getString(R.string.description2));
                    mDrinkIngre.setText(getString(R.string.ingredients2));
                    break;
                case 3:
                    mDrinkTitle.setText(getString(R.string.title3));
                    mDrinkDescr.setText(getString(R.string.description3));
                    mDrinkIngre.setText(getString(R.string.ingredients3));
                    break;
            }

            Button pourButton = (Button) rootView.findViewById(R.id.pour_button);
            pourButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(TAG, "sending data");

                    DrinkMaker.makeDrink(section);

                    sendDataToBeveryman(getActivity(), "0,1,2,5,0,0,0,6");
                }
            });
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
