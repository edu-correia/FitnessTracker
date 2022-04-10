package com.educorreia.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.educorreia.fitnesstracker.database.DatabaseHelper;
import com.educorreia.fitnesstracker.interfaces.OnItemClickListener;
import com.educorreia.fitnesstracker.models.ButtonItem;
import com.educorreia.fitnesstracker.models.RegisterItem;

import java.util.List;

public class RegistersListActivity extends AppCompatActivity {

    private RecyclerView rcvRegistersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registers_list);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String type = extras.getString("type");

            new Thread(() -> {
                DatabaseHelper db = DatabaseHelper.getInstance(this);
                List<RegisterItem> registers = db.listRegistersByType(type);

                runOnUiThread(() -> {
                    rcvRegistersList = findViewById(R.id.rcv_registers_list);
                    rcvRegistersList.setLayoutManager(new LinearLayoutManager(this));
                    RegistersListAdapter adapter = new RegistersListAdapter(getApplicationContext(), registers);
                    rcvRegistersList.setAdapter(adapter);
                });
            }).start();
        }
    }

    private class RegistersListAdapter extends RecyclerView.Adapter<RegistersListAdapter.RegisterViewHolder> {
        private Context context;
        private List<RegisterItem> registers;

        public RegistersListAdapter(Context context, List<RegisterItem> registers) {
            this.context = context;
            this.registers = registers;
        }

        @NonNull
        @Override
        public RegistersListAdapter.RegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RegistersListAdapter.RegisterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RegistersListAdapter.RegisterViewHolder holder, int position) {
            RegisterItem currentItem = registers.get(position);
            holder.bind(currentItem);
        }

        @Override
        public int getItemCount() {
            return registers.size();
        }

        private class RegisterViewHolder extends RecyclerView.ViewHolder{

            public RegisterViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(RegisterItem item){
                TextView txt = itemView.findViewById(android.R.id.text1);

                txt.setText("Valor: " + item.getValue() + " - Criado em: " + item.getCreatedAt());
            }
        }
    }
}