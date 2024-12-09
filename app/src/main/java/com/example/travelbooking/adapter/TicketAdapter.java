package com.example.travelbooking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbooking.R;
import com.example.travelbooking.model.Ticket;


import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> ticketList;

    public TicketAdapter(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket, parent, false);
        return new TicketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        holder.placeTextView.setText(ticket.getPlaceName());
        holder.countryTextView.setText(ticket.getCountryName());
        holder.priceTextView.setText(ticket.getPrice());
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView placeTextView, countryTextView, priceTextView;

        public TicketViewHolder(View itemView) {
            super(itemView);
            placeTextView = itemView.findViewById(R.id.place);
            countryTextView = itemView.findViewById(R.id.country);
            priceTextView = itemView.findViewById(R.id.price);
        }
    }
}
