package com.example.vlad.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Adapter extends ArrayAdapter. This adapter is responsible for each post view.
 * Also it implements functionality to add a like to a post.
 */
public class FeedArrayAdapter extends ArrayAdapter<Post> {
    /**
     * Map assigns an integer for each Post object.
     */
    public HashMap<Post, Integer> mPostsMap = new HashMap<>();

    public FeedArrayAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);
        for (int i = 0; i < objects.size(); i++) {
            mPostsMap.put(objects.get(i), i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post post = getItem(position);
        FeedHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.listview_item, parent, false);

            Button likeButton = (Button) convertView.findViewById(R.id.like_button);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VKParameters parameters = VKParameters.from(
                            "type", "post",
                            "owner_id", "-" + FeedActivityFragment.GROUP_ID,
                            "item_id", post.getId()
                    );
                    VKRequest request = new VKRequest("likes.add", parameters);
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            Toast.makeText(getContext(), "Like added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(VKError error) {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                            Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            holder = new FeedHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.listview_image);
            holder.message = (TextView) convertView.findViewById(R.id.listview_text);

            convertView.setTag(holder);
        } else {
            holder = (FeedHolder) convertView.getTag();
        }

        holder.message.setText(post.getMessage());
        Picasso.with(getContext()).load(post.getImageUrl()).into(holder.icon);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        Post post = getItem(position);
        return mPostsMap.get(post);
    }

    /**
     * Inner class, used for working with each post view.
     */
    static class FeedHolder {
        TextView message;
        ImageView icon;
    }
}