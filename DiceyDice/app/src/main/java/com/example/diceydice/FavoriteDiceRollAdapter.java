package com.example.diceydice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class FavoriteDiceRollAdapter extends RecyclerView.Adapter<FavoriteDiceRollAdapter.FavoriteDiceRollViewHolder> {

    private List<DiceRoll> mFavoriteDiceRolls;

    private final FavoriteDiceRollClickHandler mFavoriteDiceRollClickHandler;

    FavoriteDiceRollAdapter(FavoriteDiceRollClickHandler favoriteDiceRollClickHandler) {
        mFavoriteDiceRollClickHandler = favoriteDiceRollClickHandler;
    }


    //Inner ViewHolder Class
    public class FavoriteDiceRollViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTextView;
        TextView mDescripTextView;
        ImageButton mMoreButton;

        //Constructor
        public FavoriteDiceRollViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.favorite_name_tv);
            mDescripTextView = itemView.findViewById(R.id.favorite_descrip_tv);
            mMoreButton = itemView.findViewById(R.id.favorite_more_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            DiceRoll diceRoll = mFavoriteDiceRolls.get(adapterPosition);
            mFavoriteDiceRollClickHandler.onItemClick(diceRoll);
        }
    }

    @NonNull
    @Override
    public FavoriteDiceRollViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIDForListItem = R.layout.favorite_recycler_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIDForListItem, viewGroup, false);

        return new FavoriteDiceRollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteDiceRollViewHolder viewHolder, int position) {
        DiceRoll currentDiceRoll = mFavoriteDiceRolls.get(position);

        String name = currentDiceRoll.getName();
        String descrip = currentDiceRoll.getFormula();

        viewHolder.mNameTextView.setText(name);
        viewHolder.mDescripTextView.setText(descrip);

        //TODO Setup More-Button logic
    }

    @Override
    public int getItemCount() {
        if (mFavoriteDiceRolls == null) return 0;
        return mFavoriteDiceRolls.size();
    }

    /**
     * Helper method to set FavoriteDiceRoll List to existing FavoriteDiceRollAdapter
     *
     * @param favoriteDiceRolls the new set of FavoriteDiceRolls to be displayed
     */

    public void setFavoriteDiceRolls(List<DiceRoll> favoriteDiceRolls) {
        mFavoriteDiceRolls = favoriteDiceRolls;
        notifyDataSetChanged();
    }

    //Interface to handle clicks, defined in MainActivity
    public interface FavoriteDiceRollClickHandler {
        void onItemClick(DiceRoll favoriteDiceRoll);
    }
}