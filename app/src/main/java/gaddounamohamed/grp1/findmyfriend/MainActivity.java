package gaddounamohamed.grp1.findmyfriend;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import gaddounamohamed.grp1.findmyfriend.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Check and request permissions
        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
        };

        boolean needsPermission = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needsPermission = true;
                break;
            }
        }

        if (needsPermission) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean smsGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean locationGranted = grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED;

                if (smsGranted && locationGranted) {
                    Toast.makeText(this, "Toutes les permissions accordées", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Certaines permissions refusées. L'application peut ne pas fonctionner correctement.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}