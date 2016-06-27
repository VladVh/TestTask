package com.example.vlad.test;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment creates request to VK API and shows page's feed returned by response.
 */
public class FeedActivityFragment extends Fragment {
    /**
     * Feed is retrieved from this page
     */
    public static final int GROUP_ID = 34613199;
    /**
     * Number of posts to retrieve
     */
    public static final int FEED_COUNT = 20;
    /**
     * Shows a list of posts on a screen
     */
    private ListView mListView;


    public FeedActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_feed, container, false);
        mListView = (ListView) rootview.findViewById(R.id.listview);

        readFeed();

        return rootview;
    }

    /**
     * Retrieves feed from the given page
     */
    private void readFeed() {
        VKParameters parameters = VKParameters.from(
                VKApiConst.OWNER_ID, "-" + GROUP_ID,
                VKApiConst.COUNT, "" + FEED_COUNT
        );

        VKRequest request = new VKRequest("wall.get", parameters);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                //VKList<VKApiPost> posts = (VKList<VKApiPost>) response.parsedModel;
                List<Post> posts = parseResponse(response);
                FeedArrayAdapter adapter = new FeedArrayAdapter(getContext(), R.layout.listview_item, posts);
                mListView.setAdapter(adapter);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }

    /**
     * Obtains posts from response
     * @param response response to a request sent
     * @return list of posts
     * throws JSONException on parse error
     */
    private List<Post> parseResponse(VKResponse response) {
        List<Post> posts = new ArrayList<>();
        try {
            JSONObject object = response.json.getJSONObject("response");
            JSONArray items = object.getJSONArray("items");

            JSONObject item;
            for (int i = 0; i < items.length(); i++) {
                try {
                    item = items.getJSONObject(i);
                    Post post = new Post(item.getInt("id"),
                            item.getString("text"),
                            Uri.parse(item.getJSONArray("attachments")
                                    .getJSONObject(0)
                                    .getJSONObject("photo")
                                    .getString("photo_604")));
                    posts.add(post);
                } catch (JSONException e) {
                    Log.e("Wrong format", "Wrong format " + items.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
