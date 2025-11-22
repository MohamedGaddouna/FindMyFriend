package gaddounamohamed.grp1.findmyfriend.ui.notifications;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import gaddounamohamed.grp1.findmyfriend.Config;
import gaddounamohamed.grp1.findmyfriend.R;

public class NotificationsFragment extends Fragment {

    EditText edtPseudo, edtNumero, edtLongitude, edtLatitude;
    Button btnAddPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        edtPseudo = root.findViewById(R.id.edtPseudo);
        edtNumero = root.findViewById(R.id.edtNumero);
        edtLongitude = root.findViewById(R.id.edtLongitude);
        edtLatitude = root.findViewById(R.id.edtLatitude);
        btnAddPosition = root.findViewById(R.id.btnAddPosition);

        btnAddPosition.setOnClickListener(v -> {
            String pseudo = edtPseudo.getText().toString().trim();
            String numero = edtNumero.getText().toString().trim();
            String longitude = edtLongitude.getText().toString().trim();
            String latitude = edtLatitude.getText().toString().trim();

            if (pseudo.isEmpty() || numero.isEmpty() || longitude.isEmpty() || latitude.isEmpty()) {
                Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = Config.URL_Add_Location + "?pseudo=" + pseudo +
                    "&numero=" + numero +
                    "&longitude=" + longitude +
                    "&latitude=" + latitude;

            new AddPositionTask().execute(url);
        });

        return root;
    }

    private class AddPositionTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Ajout en cours...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                return response.toString();
            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) dialog.dismiss();

            if (result.contains("\"success\":1")) {
                Toast.makeText(getActivity(), "Position ajoutée avec succès ", Toast.LENGTH_LONG).show();
                edtPseudo.setText("");
                edtNumero.setText("");
                edtLongitude.setText("");
                edtLatitude.setText("");
            } else {
                Toast.makeText(getActivity(), "Échec de l’ajout  : " + result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
