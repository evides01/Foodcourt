package th.com.samsen.tunyaporn.foodcourt.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import th.com.samsen.tunyaporn.foodcourt.R;

public class MenuButtonView extends FrameLayout {

    private TextView button;
    private TextView textView;

    public MenuButtonView(@NonNull Context context) {
        super(context);
        initInflate();
    }

    public MenuButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initInflate();
    }

    public MenuButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
    }

    public String getFoodName() {
        return button.getText().toString();
    }

    public void setFoodName(String foodName) {
        button.setText(foodName);
    }

    private String getPrice(){
        return textView.getText().toString();
    }

    public void setPrice(String price) {
        textView.setText(price);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.content_button, this);
        button = findViewById(R.id.button_food1);
        textView = findViewById(R.id.txt_price);
    }
}
