package se.tims.maps;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by timmieS on 27/04/16.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>
{
    private final Context context;
    private final List<Address> addresses;

    public PlaceAdapter(Context context, List<Address> addresses)
    {
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Address address = addresses.get(position);

        holder.setAddress(context, address);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public static final class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Context context;
        private Address address;
        private final TextView name_text;
        private final TextView country_text;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            this.name_text = (TextView) itemView.findViewById(R.id.name_text);
            this.country_text = (TextView) itemView.findViewById(R.id.country_text);
            itemView.setOnClickListener(this);
        }

        public void setAddress(Context context, Address address)
        {
            this.context = context;
            this.address = address;
            name_text.setText(address.getFeatureName());
            country_text.setText(address.getCountryName());
        }

        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(context, MapsActivity.class);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            intent.putExtra("MARKER", (Parcelable)latLng);

            context.startActivity(intent);
        }
    }
}
