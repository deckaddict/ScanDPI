package se.deckaddict.android.ScanDPI;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

import android.widget.Toast;
import android.content.Context;

public class XposedHook implements IXposedHookLoadPackage
{
	public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
		// Ensure that we add a hook in the correct package.
		if (!lpparam.packageName.equals("com.hp.printercontrol")) {
            return;
		}
		XposedBridge.log("ScanDPI::HP All-in-One Printer Remote loaded.");

		findAndHookMethod("com.hp.printercontrol.scan.UiScannerAct", lpparam.classLoader, "getPrefs", java.lang.String.class, java.lang.String.class, new XC_MethodHook() {
				// Inject code after the method call.
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					// Resolution is the key for the DPI settings.
					if (param.args[0].toString().equals("Resolution")) {

						XSharedPreferences prefs = new XSharedPreferences("se.deckaddict.android.ScanDPI", "settings");
						Integer dpi = prefs.getInt("dpi", 0);

						// Using 600 as default resolution if app has not been run.
						if (dpi == null) {
							dpi = 600;
						}

						XposedBridge.log("ScanDPI::Scanner DPI requested. Replaced by ScanDPI: " + dpi.toString());

						// Display toast to ensure that user is reminded of the override.
						Context context = (Context) XposedHelpers.callMethod(param.thisObject, "getApplicationContext");
						param.setResult(dpi.toString());
						CharSequence text = "Using resolution from ScanDPI: " + dpi.toString();
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				}
			});
    }
}

