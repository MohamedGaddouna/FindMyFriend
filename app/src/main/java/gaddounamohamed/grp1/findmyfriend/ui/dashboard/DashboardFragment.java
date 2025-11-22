package gaddounamohamed.grp1.findmyfriend.ui.dashboard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import gaddounamohamed.grp1.findmyfriend.MapsActivity;
import gaddounamohamed.grp1.findmyfriend.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String numero = binding.etNum.getText().toString();
                SmsManager manager= SmsManager.getDefault();
                manager.sendTextMessage(numero,null,"findMYFriend envoyer moi votre position ",null,null);
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}