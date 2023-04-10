package com.example.media_player;

import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SoundActivity extends AppCompatActivity {

    ArrayList<List_item> listitems = new ArrayList<>();

    int[] Photos = {R.drawable.chat, R.drawable.chien, R.drawable.vache};
    String[] Titles = {"Chat","Chien","Vache"};
    int[] MP3Sounds = {R.raw.crie_chat, R.raw.crie_chien, R.raw.crie_vache};

    ListView listView;
    MediaPlayer sound = new MediaPlayer();
    private SeekBar seekBar;
    Button btn_play, btn_pause, btn_stop;
    TextView tvTitle, tvCurrentTime, tvTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        listView = findViewById(R.id.listView2);
        seekBar = findViewById(R.id.seekBar);
        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);
        btn_stop =  findViewById(R.id.btn_stop);

        tvTitle = findViewById(R.id.tvTitle);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);


        for (int i = 0; i < Photos.length; i++) {
            listitems.add(new List_item(Titles[i], Photos[i], MP3Sounds[i]));
        }

        listAdapter listAdapter = new listAdapter(listitems);
        listView.setAdapter(listAdapter);

        sound = MediaPlayer.create(SoundActivity.this, MP3Sounds[0]);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                sound.stop();
                sound.reset();
                sound = MediaPlayer.create(SoundActivity.this, listitems.get(i).sound);
                tvTitle.setText(listitems.get(i).getTitle());
                SoundTime();

            }
        });


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sound.isPlaying()) {
                    Thread updateSeekBar;
                    updateSeekBar = new Thread() {
                        @Override
                        public void run() {
                            int SoundDuration = sound.getDuration();
                            int currentPostion = 0;
                            seekBar.setMax(SoundDuration);
                            while (currentPostion < SoundDuration) {
                                try {
                                    sleep(100);
                                    currentPostion = sound.getCurrentPosition();
                                    seekBar.setProgress(currentPostion);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    sound.start();
                    updateSeekBar.start();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.stop();

            }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound.pause();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) sound.seekTo(i);
                SoundTime();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void SoundTime() {
        seekBar.setMax(sound.getDuration());
        int tim = (seekBar.getMax() / 1000);
        int m = tim / 60;
        int s = tim % 60;

        int tim0 = (seekBar.getProgress() / 1000);
        int m0 = tim0 / 60;
        int s0 = tim0 % 60;

        tvTotalTime.setText(s + " : " + m);
        tvCurrentTime.setText(s0 + " : " + m0);
    }



    class listAdapter extends BaseAdapter {

        ArrayList<List_item> lis = new ArrayList<>();

        public listAdapter(ArrayList<List_item> lis) {
            this.lis = lis;
        }

        @Override
        public int getCount() {
            return lis.size();
        }

        @Override
        public Object getItem(int position) {
            return lis.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            final View view = layoutInflater.inflate(R.layout.row_item, null);
            final ImageView img = view.findViewById(R.id.imageView);
            TextView title = view.findViewById(R.id.textView_title);

            title.setText(lis.get(i).getTitle());

            Picasso.get().load(lis.get(i).getImg()).into(img);
            return view;
        }
    }

}