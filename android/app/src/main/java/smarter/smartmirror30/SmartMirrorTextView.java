package smarter.smartmirror30;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ric on 03/03/2017.
 */
public class SmartMirrorTextView extends TextView {
    public SmartMirrorTextView(Context context) {
        super(context);
        init(context);
    }

    public SmartMirrorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SmartMirrorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Regular.ttf");
        this.setTypeface(typeface);
    }

}
