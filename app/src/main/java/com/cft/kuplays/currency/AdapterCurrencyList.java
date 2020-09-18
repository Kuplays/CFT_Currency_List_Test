package com.cft.kuplays.currency;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterCurrencyList extends BaseAdapter {
    private Context context;
    private List<CurrencyItem> listViewItems;

    public AdapterCurrencyList(Context context, List<CurrencyItem> listViewItems) {
        this.context = context;
        this.listViewItems = listViewItems;
    }

    @Override
    public int getCount() {
        return this.listViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListItem customListItem = null;

        if (convertView != null) {
            customListItem = (CustomListItem)convertView.getTag();
        } else {
            customListItem = new CustomListItem();
            LayoutInflater layoutInfiater =
                    (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInfiater.inflate(R.layout.layout_listview_item, null);
            customListItem.setTvName((TextView)convertView.findViewById(R.id.tvNameField));
            customListItem.setTvCharCode((TextView)convertView.findViewById(R.id.tvCharCode));
            customListItem.setTvValue((TextView)convertView.findViewById(R.id.tvValue));
            customListItem.setTvPreviousValue((TextView)convertView.findViewById(R.id.tvPreviousValue));
            customListItem.setTvPercentage((TextView)convertView.findViewById(R.id.tvPercentChange));
            customListItem.setImvArrow((ImageView)convertView.findViewById(R.id.imgArrow));
            convertView.setTag(customListItem);
        }

        customListItem.setTvNameText(String.valueOf(listViewItems.get(position).getNominal())
                + " " + listViewItems.get(position).getName());
        customListItem.setTvCharCodeText(listViewItems.get(position).getCharCode());
        customListItem.setTvValueText(String.format("%.3f", listViewItems.get(position).getDisplayValue()));
        customListItem.setTvPreviousValueText(String.format("%.3f", listViewItems.get(position).getDisplayPreviousValue()));
        listViewItems.get(position).setPercentageChange();
        customListItem.setTvPercentageText(listViewItems.get(position).getPercentageChangeFormat());

        if (listViewItems.get(position).isHasIncreased()) {
            customListItem.setTvPercentageColor(context.getResources().getColor(R.color.colorGreen));
            customListItem.setImgArrowSrc(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_up));
        }
        else {
            customListItem.setTvPercentageColor(context.getResources().getColor(R.color.colorRed));
            customListItem.setImgArrowSrc(BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_down));
        }

        return convertView;
    }
}

class CustomListItem {
    private TextView tvName;
    private TextView tvCharCode;
    private TextView tvValue;
    private TextView tvPreviousValue;
    private TextView tvPercentage;
    private ImageView imvArrow;

    public void setTvNameText(String text) {
        this.tvName.setText(text);
    }
    public TextView getTvName() {
        return tvName;
    }
    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }

    public TextView getTvCharCode() {
        return tvCharCode;
    }

    public void setTvCharCode(TextView tvCharCode) {
        this.tvCharCode = tvCharCode;
    }

    public void setTvCharCodeText(String text) {
        this.tvCharCode.setText(text);
    }

    public TextView getTvValue() {
        return tvValue;
    }

    public void setTvValue(TextView tvValue) {
        this.tvValue = tvValue;
    }

    public void setTvValueText(String text) {
        this.tvValue.setText(text);
    }

    public TextView getTvPreviousValue() {
        return tvPreviousValue;
    }

    public void setTvPreviousValue(TextView tvPreviousValue) {
        this.tvPreviousValue = tvPreviousValue;
    }

    public void setTvPreviousValueText(String text) {
        this.tvPreviousValue.setText(text);
    }

    public TextView getTvPercentage() {
        return tvPercentage;
    }

    public void setTvPercentage(TextView tvPercentage) {
        this.tvPercentage = tvPercentage;
    }

    public void setTvPercentageText(String text) {
        this.tvPercentage.setText(text);
    }

    public void setTvPercentageColor(int color) {
        this.tvPercentage.setTextColor(color);
    }

    public ImageView getImvArrow() {
        return imvArrow;
    }

    public void setImvArrow(ImageView imvArrow) {
        this.imvArrow = imvArrow;
    }

    public void setImgArrowSrc(Bitmap bitmap) {
        this.imvArrow.setImageBitmap(bitmap);
    }
}
