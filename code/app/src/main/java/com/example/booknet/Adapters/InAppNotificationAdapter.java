package com.example.booknet.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Activities.ListingViewActivity;
import com.example.booknet.Activities.OwnListingViewActivity;
import com.example.booknet.Constants.NotificationType;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Dialogs.ReviewCreateDialog;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.InAppNotification;
import com.example.booknet.Model.InAppNotificationList;
import com.example.booknet.R;

public class InAppNotificationAdapter extends RecyclerView.Adapter<InAppNotificationAdapter.NotificationViewHolder> {

    //The list of BookListings to display
    private InAppNotificationList inAppNotificationList;

    //The activity this adapter was created from
    private FragmentActivity sourceActivity;

    DatabaseManager manager = DatabaseManager.getInstance();

    public InAppNotificationAdapter(InAppNotificationList inAppNotificationList, FragmentActivity sourceActivity) {
        Log.d("seanTag", "Construct adaptor");
        this.inAppNotificationList = inAppNotificationList;
        this.sourceActivity = sourceActivity;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_item_list, viewGroup, false);
        InAppNotificationAdapter.NotificationViewHolder newNotificationViewHolder = new InAppNotificationAdapter.NotificationViewHolder(view);

        //Log.d("seanTag", "inAppNotificationList onCreateView");

        return newNotificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder notificationViewHolder, int position) {
        //Get the inAppNotificationList at the provided position
        final InAppNotification item = inAppNotificationList.getNotificationAtPosition(position);

        //Fill the text fields with the object's inAppNotificationList
        //bookListingViewHolder.bookThumbnail.//todo apply photo
        //notificationViewHolder.notificationBookThumbnail.setImageResource(R.mipmap.ic_launcher);
        notificationViewHolder.notificationBookTitle.setText(item.getRequestedBookListing().getBook().getTitle());
        notificationViewHolder.notificationUsername.setText(item.getUserMakingNotification());
        notificationViewHolder.notificationUserInfo.setText(item.getNotificationType().toString());

        Log.d("seanTag", "onbindviewholder " + item.getRequestedBookListing().getBook().getTitle());

        notificationViewHolder.notificationStatus.setText(item.getRequestedBookListing().getStatus().toString());
        notificationViewHolder.item = item;

        /*if ((position & 1) == 1) {//check odd
            notificationViewHolder.notificationBody.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }*/

        //Add the click listener to the item
        notificationViewHolder.notificationBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Expand Buttons
                boolean isExpanded = (notificationViewHolder.expandButtons.getVisibility() == View.VISIBLE);
                if (isExpanded) {
                    notificationViewHolder.expandButtons.animate();
                    notificationViewHolder.expandButtons.setVisibility(View.GONE);
                } else {
                    notificationViewHolder.expandButtons.setVisibility(View.VISIBLE);
                }
            }
        });
        notificationViewHolder.notificationBody.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("jamie", "notification focus");
                if (!hasFocus) {
                    Log.d("jamie", "notification lost focus");
                    notificationViewHolder.expandButtons.setVisibility(View.GONE);
                }
            }
        });
        notificationViewHolder.dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNotification(item);
            }
        });

        notificationViewHolder.gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (item.getNotificationType() == NotificationType.hasRequested) {
                    intent = new Intent(sourceActivity, OwnListingViewActivity.class);
                    if (item != null) {
                        intent.putExtra("isbn", item.getRequestedBookListing().getBook().getIsbn());
                        intent.putExtra("dupID", item.getRequestedBookListing().getDupInd());
                    }
                    sourceActivity.startActivity(intent);
                } else if (item.getNotificationType() == NotificationType.canReview) {
                    ReviewCreateDialog reviewCreateDialog = ReviewCreateDialog.newInstance(CurrentUser.getInstance().getUsername(), item.getUserMakingNotification());
                    reviewCreateDialog.show(sourceActivity.getSupportFragmentManager(), "Create Review");
                } else {
                    intent = new Intent(sourceActivity, ListingViewActivity.class);
                    if (item != null) {
                        intent.putExtra("ownerUsername", item.getRequestedBookListing().getOwnerUsername());
                        intent.putExtra("isbn", item.getRequestedBookListing().getBook().getIsbn());
                        intent.putExtra("dupID", item.getRequestedBookListing().getDupInd());
                    }
                    sourceActivity.startActivity(intent);
                }


                //removeNotification(item);//do not actually
            }
        });
    }

    private void removeNotification(InAppNotification inAppNotification) {
        manager.removeNotification(inAppNotification);
        inAppNotificationList.removeNotification(inAppNotification);
        notifyDataSetChanged();
    }

    /**
     * Override to get the number of items in the dataset
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return inAppNotificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ConstraintLayout notificationBody;
        private ImageView notificationBookThumbnail;
        private TextView notificationBookTitle;
        private TextView notificationUserInfo;
        private TextView notificationUsername;
        private TextView notificationStatus;
        private ConstraintLayout expandButtons;
        private Button dismissButton;
        private Button gotoButton;
        private InAppNotification item;

        /**
         * Creates the BookListingViewHolder
         *
         * @param notificationView The view for this item
         */
        public NotificationViewHolder(@NonNull View notificationView) {
            super(notificationView);

            //Obtain Layout Object References
            notificationBody = notificationView.findViewById(R.id.notificationBody);
            notificationBookThumbnail = notificationView.findViewById(R.id.notificationBookThumbnail);
            notificationBookTitle = notificationView.findViewById(R.id.notificationBookTitle);
            notificationUserInfo = notificationView.findViewById(R.id.notificationUserInfo);
            notificationUsername = notificationView.findViewById(R.id.notificationUsername);
            notificationStatus = notificationView.findViewById(R.id.notificationStatus);
            expandButtons = notificationView.findViewById(R.id.expandButtons);
            dismissButton = notificationView.findViewById(R.id.dismissButton);
            gotoButton = notificationView.findViewById(R.id.gotoButton);
        }
    }
}
