package smarter.smartmirror30;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class NewsFragment extends Fragment {

    private TextView[] headlines;
    Values values;

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        values = Values.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);
        headlines = new TextView[4];
        headlines[0] = (TextView) root.findViewById(R.id.headlineTxt1);
        headlines[1] = (TextView) root.findViewById(R.id.headlineTxt2);
        headlines[2] = (TextView) root.findViewById(R.id.headlineTxt3);
        headlines[3] = (TextView) root.findViewById(R.id.headlineTxt4);
        String[] defaultHeadlines = {"Hello", "This", "Is", "News"};
        updateNews(defaultHeadlines);
        return root;
    }

    public void updateNews(String[] headlineStrings){
        if (headlineStrings!=null) {
            for (int i = 0; i < headlines.length; i++) {
                headlines[i].setText(headlineStrings[i]);
            }
        }
    }

}
