package es.app.attune.attune.Classes;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import es.app.attune.attune.R;

public class ErrorSnackbar {
    private Snackbar snackbar;

    public ErrorSnackbar(View view) {
        // styling for action text
        snackbar = snackbar.make(view, "", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);

        // styling for rest of text
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);

        // styling for background of snackbar
        View sbView = snackbarView;
        sbView.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
    }

    public void setSnackBarText(String text){
        snackbar.setText(text);
    }

    public void showSnackBar(){
        snackbar.show();
    }
}
