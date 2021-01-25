package th.com.samsen.tunyaporn.foodcourt.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class DisableSoftKeyBoardEditText extends EditText {
    public DisableSoftKeyBoardEditText(Context context) {
        super(context);
    }

    public DisableSoftKeyBoardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return false;
    }

    public DisableSoftKeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
