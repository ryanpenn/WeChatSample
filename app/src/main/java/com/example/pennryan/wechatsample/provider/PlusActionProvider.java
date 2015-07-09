package com.example.pennryan.wechatsample.provider;

import android.content.Context;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.example.pennryan.wechatsample.R;

/**
 * Created by pennryan on 15/7/9.
 */
public class PlusActionProvider extends ActionProvider {

    private Context context;

    public PlusActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();

        subMenu.add(context.getString(R.string.plus_group_chat))
                .setIcon(R.mipmap.ofm_group_chat_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

        subMenu.add(context.getString(R.string.plus_add_friend))
                .setIcon(R.mipmap.ofm_add_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

        subMenu.add(context.getString(R.string.plus_video_chat))
                .setIcon(R.mipmap.ofm_video_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

        subMenu.add(context.getString(R.string.plus_scan))
                .setIcon(R.mipmap.ofm_qrcode_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

        subMenu.add(context.getString(R.string.plus_take_photo))
                .setIcon(R.mipmap.ofm_camera_icon)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
