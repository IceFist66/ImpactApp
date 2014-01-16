package ewbcalpoly.impact.ewbimpact;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<Event> {
	
	    private ArrayList<Event> entries;
	    private Activity activity;
	 
	    public CustomArrayAdapter(Activity a, int textViewResourceId, ArrayList<Event> entries) {
	        super(a, textViewResourceId, entries);
	        this.entries = entries;
	        this.activity = a;
	    }
	 
	    public static class ViewHolder{
	        public TextView item1;
	        public TextView item2;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	        ViewHolder holder;
	        if (v == null) {
	            LayoutInflater vi =
	                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.grid_item, null);
	            holder = new ViewHolder();
	            holder.item1 = (TextView) v.findViewById(R.id.big);
	            holder.item2 = (TextView) v.findViewById(R.id.small);
	            v.setTag(holder);
	        }
	        else
	            holder=(ViewHolder)v.getTag();
	 
	        final Event custom = entries.get(position);
	        if (custom != null) {
	            holder.item1.setText(custom.getName());
	            holder.item2.setText("Start: " + custom.getStart() + "\n" + "End: " + custom.getStop());
	        }
	        return v;
	        
	        
	    }
	 
	}


