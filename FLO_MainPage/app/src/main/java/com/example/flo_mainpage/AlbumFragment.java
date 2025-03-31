package com.example.flo_mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlbumFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. UI 요소 가져오기
        ImageView albumCover = view.findViewById(R.id.imageAlbumCover);
        TextView albumTitle = view.findViewById(R.id.textAlbumTitle);
        TextView artistName = view.findViewById(R.id.textArtist);
        TextView releaseDate = view.findViewById(R.id.textReleaseDate);
        Switch mixToggle = view.findViewById(R.id.toggleMix);

        // 2. 데이터 설정
        albumCover.setImageResource(R.drawable.img_album_exp2);  // 앨범 커버
        albumTitle.setText("IU 5th Album 'LILAC'");           // 앨범 제목
        artistName.setText("아이유 (IU)");                     // 아티스트
        releaseDate.setText("2021.03.25 | 장르: 댄스 팝");    // 발매일

        // 3. 토글 버튼 이벤트 처리
        mixToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), isChecked ? "MIX ON" : "MIX OFF", Toast.LENGTH_SHORT).show();
        });
    }
}
