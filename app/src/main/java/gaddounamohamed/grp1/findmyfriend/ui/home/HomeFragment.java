package gaddounamohamed.grp1.findmyfriend.ui.home;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import gaddounamohamed.grp1.findmyfriend.Config;
import gaddounamohamed.grp1.findmyfriend.R;

public class HomeFragment extends Fragment {

    Button btnDownload;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnDownload = root.findViewById(R.id.btnDownload);
        listView = root.findViewById(R.id.listView);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        btnDownload.setOnClickListener(v -> new DownloadTask().execute());

        return root;
    }

    // =============================
    // 🌐 Classe interne AsyncTask
    // =============================
    private class DownloadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Téléchargement des positions...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(Config.URL_GetAll_Locations);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @Override protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss(); // ✅ évite le WindowLeaked
            }

            if (result.startsWith("Exception") || result.startsWith("Error")) {
                Toast.makeText(getActivity(), "Échec du téléchargement : " + result, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                // ✅ Le JSON que tu as montré contient un objet racine, pas un tableau
                JSONObject root = new JSONObject(result);

                if (root.has("positions")) {
                    JSONArray arr = root.getJSONArray("positions");

                    dataList.clear(); // vider la liste avant d'ajouter

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String line = "Pseudo: " + obj.getString("pseudo") +
                                "\nNuméro: " + obj.getString("numero") +
                                "\nLatitude: " + obj.getString("latitude") +
                                "\nLongitude: " + obj.getString("longitude");
                        dataList.add(line);
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Aucune donnée trouvée", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Erreur JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
