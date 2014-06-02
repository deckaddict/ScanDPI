package se.deckaddict.android.ScanDPI;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.content.SharedPreferences;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
			
		/* Using getSharedPreferences in order to
		operate with XSharedPreferences in XposedHook */
		SharedPreferences storedValues = getSharedPreferences("settings", MODE_WORLD_READABLE);
		Integer dpi = storedValues.getInt("dpi", 0);

		/* Setting is null when app is run for the first time.
		Choosing 600 as default as an increment of the maximum
		is a natural reason to install the app. */
		if (dpi == null) {
			dpi = 600;
		}
		
		// Display initial value
		final EditText eDPI;
		eDPI = (EditText) findViewById(R.id.editdpi);
		eDPI.setText(dpi.toString());
		
		// Listen and trigger on each modification of the value.
		eDPI.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.length() != 0) {
					SharedPreferences.Editor pEditor = getSharedPreferences("settings", MODE_WORLD_READABLE).edit();
					pEditor.putInt("dpi", Integer.parseInt(s.toString()));
					pEditor.commit();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		});
	}
	
}
