package com.example.mangurkaur.employeeattendancepro.Activties;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mangurkaur.employeeattendancepro.R;

import java.util.ArrayList;
import java.util.List;

public class EmpAdapter  extends  RecyclerView.Adapter<EmpAdapter.MyViewHolder> {


    private List<EmpClass> empList=new ArrayList<>();
    private Context c;


    public EmpAdapter(List<EmpClass> empList,Context c) {

        this.empList = empList;

        this.c=c;


    }



    @NonNull
    @Override
    public EmpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.emp_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmpAdapter.MyViewHolder holder, final int position) {
        final EmpClass empClass=empList.get(position);

        holder.empName.setText(empClass.getFirstName());
        //holder.present.setVisibility(View.GONE);
        if(empClass.getStatus().equals("Active")){

            holder.present.setVisibility(View.VISIBLE);

            holder.absent.setVisibility(View.GONE);

        }
        else if(empClass.getStatus().equals("InActive")){

            holder.present.setVisibility(View.GONE);
            holder.absent.setVisibility(View.VISIBLE);

        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent screen=new Intent(c,InformationActivity.class);
                screen.putExtra("EmpId",empClass.getEmpId());

                c.startActivity(screen);

            }

        });


    }

    @Override
    public int getItemCount() {

        return empList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView empName;
        RelativeLayout layout;
        ImageView present;
        ImageView absent;
        public MyViewHolder(View itemView) {
            super(itemView);
            c=itemView.getContext();
            empName=(TextView)itemView.findViewById(R.id.empName);
            layout=(RelativeLayout)itemView.findViewById(R.id.layout_emp_row);
            present=(ImageView)itemView.findViewById(R.id.present);
            absent=(ImageView)itemView.findViewById(R.id.absent);
        }
    }


}
