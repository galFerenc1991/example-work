package com.kubator.pamp.presentation.utils;

import android.net.Uri;

import com.kubator.pamp.PampApp_;
import com.kubator.pamp.R;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

/**
 * Created by
 * Ferenc on 2017.12.07..
 */

public abstract class FirebaseDynamicLinkGenerator {

    private static Uri shortLink;

    public static Uri getDynamicLink(String _id) {
        String appCode = PampApp_.getInstance().getString(R.string.app_code);

        Uri.Builder builder = new Uri.Builder()
                .scheme("https")
                .authority(appCode + ".app.goo.gl")
                .path("/")
                .appendQueryParameter("link", "http://pampconnect.com/?id=" + _id)
                .appendQueryParameter("apn", "com.kubator.pamp")
                .appendQueryParameter("ibi", "com.1kubator.pamp")
                .appendQueryParameter("afl", "https://play.google.com/store/apps/details?id=com.kubator.pamp")
                .appendQueryParameter("ifl", "https://itunes.apple.com/fr/app/id1296281729?mt=8/");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(builder.build())
                .buildDynamicLink();

        return dynamicLink.getUri();
    }

    public static Uri getShortDynamicLink() {
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLongLink(Uri.parse(getDynamicLink().toString()))
//                .buildShortDynamicLink()
//                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                        } else {
//                            // Error
//                            // ...
//                        }
//                    }
//                });
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://pampconnect.com/deeplink"))
                .setDynamicLinkDomain("appCode" + ".app.goo.gl")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
        return shortLink;
    }
}
