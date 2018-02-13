package com.ferenc.pamp.presentation.screens.main.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.auth.AuthActivity_;
import com.ferenc.pamp.presentation.screens.main.profile.about.AboutActivity;
import com.ferenc.pamp.presentation.screens.main.profile.about.AboutActivity_;
import com.ferenc.pamp.presentation.screens.main.profile.edit_profile.EditProfileActivity_;
import com.ferenc.pamp.presentation.screens.main.profile.my_orders.MyOrdersActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.RoundedTransformation;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.04..
 */
@EFragment
public class ProfileFragment extends ContentFragment implements ProfileContract.View {

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_profile;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private ProfileContract.Presenter mPresenter;

    @ViewById(R.id.ivProfilePicture_FP)
    protected ImageView ivProfilePicture;
    @ViewById(R.id.tvName_FP)
    protected TextView tvName;
    @ViewById(R.id.rlProfile_information_FP)
    protected RelativeLayout rlProfileInformation;
    @ViewById(R.id.rlMyOrders_FP)
    protected RelativeLayout rlMyOrders;
    @ViewById(R.id.rlAbout_FP)
    protected RelativeLayout rlAbout;
    @ViewById(R.id.rlShare_FP)
    protected RelativeLayout rlShare;
    @ViewById(R.id.rlConnect_FP)
    protected RelativeLayout rlConnect;
    @ViewById(R.id.rlLogOut_FP)
    protected RelativeLayout rlLogOut;

    @Bean
    protected AuthRepository mAuthRepository;
    @Bean
    protected UserRepository mUserRepository;
    @Bean
    protected SignedUserManager mUserManager;
    @Bean
    protected UserRelay mUserRelay;

    @StringRes(R.string.msg_share)
    protected String mShareMessage;

    @AfterInject
    @Override
    public void initPresenter() {
        new ProfilePresenter(this, mAuthRepository, mUserRepository, mUserManager, mUserRelay);
    }

    @AfterViews
    protected void initUI() {

        RxView.clicks(rlLogOut)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedLogOut());

        RxView.clicks(rlProfileInformation)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedProfileInformation());

        RxView.clicks(rlShare)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedSharePamp());

        RxView.clicks(rlMyOrders)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedMyOrders());

        RxView.clicks(rlAbout)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickAbout());

        RxView.clicks(rlConnect)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickContactUs());
        mPresenter.subscribe();
    }

    @Override
    public void openEditProfile() {
        EditProfileActivity_.intent(this).start();
    }

    @Override
    public void setUserProfilePictureAndName(String _avatarUrl, String _name) {
        Picasso.with(PampApp_.getInstance())
                .load(_avatarUrl)
                .placeholder(R.drawable.ic_userpic)
                .error(R.drawable.ic_userpic)
                .transform(new RoundedTransformation(200, 0))
                .fit()
                .centerCrop()
                .into(ivProfilePicture);

        tvName.setText(_name);
    }

    @Override
    public void sharePamp() {
        Intent it = new Intent(Intent.ACTION_SENDTO);
        it.setData(Uri.parse("smsto:"));
        it.putExtra("sms_body", mShareMessage);
        startActivity(it);
    }

    @Override
    public void showMyOrders() {
        MyOrdersActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void openMailSender() {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("email@gmail.com") +
                "?subject=" + Uri.encode("the subject") +
                "&body=" + Uri.encode("the body of the message");
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        startActivity(Intent.createChooser(send, "Send mail..."));
    }

    @Override
    public void openAbout() {
        AboutActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void logOut() {
        AuthActivity_.intent(this)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
                mPresenter.unsubscribe();

    }

}
