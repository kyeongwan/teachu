package kr.ac.ssu.teachu.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import kr.ac.ssu.teachu.R;
import kr.ac.ssu.teachu.model.Board;
import kr.ac.ssu.teachu.util.AppController;

public class MoreDialogFragment extends DialogFragment {

    Board board;

    public MoreDialogFragment(Board board){
        this.board = board;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moreview, container);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_more_title);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_more_context);
        NetworkImageView networkImageView = (NetworkImageView) view.findViewById(R.id.iv_moew_img);

        tvTitle.setText(board.title);
        tvContent.setText(board.context);
        networkImageView.setImageUrl(board.imageUrl, AppController.getInstance().getImageLoader());

        Button btClose = (Button) view.findViewById(R.id.bt_moewview_close);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        // 레이아웃 XML과 뷰 변수 연결

        return view;
    }
}