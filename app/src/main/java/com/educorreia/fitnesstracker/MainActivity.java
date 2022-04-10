package com.educorreia.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.educorreia.fitnesstracker.interfaces.OnItemClickListener;
import com.educorreia.fitnesstracker.models.ButtonItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View btnImc;
    private RecyclerView rvBtnsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ButtonItem> buttons = new ArrayList<>();
        buttons.add(new ButtonItem(1, R.string.imc, R.drawable.ic_scale, R.color.dark_gray ));
        buttons.add(new ButtonItem(2, R.string.tmb, R.drawable.ic_food, R.color.dark_gray ));

        rvBtnsList = findViewById(R.id.rcv_btns_list);
        rvBtnsList.setLayoutManager(new GridLayoutManager(this, 2));
        ButtonsListAdapter adapter = new ButtonsListAdapter(getApplicationContext(), buttons);
        adapter.setListener((id) -> {
            switch (id){
                case 1:
                    startActivity(new Intent(MainActivity.this, ImcActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, TmbActivity.class));
                    break;
            }

        });
        rvBtnsList.setAdapter(adapter);
    }

    private class ButtonsListAdapter extends RecyclerView.Adapter<ButtonsListAdapter.ButtonViewHolder> {
        private Context context;
        private List<ButtonItem> buttons;
        private OnItemClickListener listener;

        public ButtonsListAdapter(Context context, List<ButtonItem> buttons) {
            this.context = context;
            this.buttons = buttons;
        }

        public void setListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.btns_list_item, parent, false);
            return new ButtonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
            ButtonItem currentItem = buttons.get(position);
            holder.bind(currentItem);
        }

        @Override
        public int getItemCount() {
            return buttons.size();
        }

        private class ButtonViewHolder extends RecyclerView.ViewHolder{

            public ButtonViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(ButtonItem item){
                TextView txt = itemView.findViewById(R.id.txt_btn_title);
                ImageView img = itemView.findViewById(R.id.img_btn_image);
                LinearLayout button = (LinearLayout) itemView.findViewById(R.id.btn_action);

                button.setOnClickListener((view) -> {
                    listener.onClick(item.getId());
                });

                txt.setText(item.getTitle());
                img.setImageResource(item.getDrawableId());
                button.setBackgroundColor(item.getColor());
            }
        }
    }
}