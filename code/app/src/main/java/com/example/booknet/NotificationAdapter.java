package com.example.booknet;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    //The list of BookListings to display
    private Notifications notifications;

    //The activity this adapter was created from
    private FragmentActivity sourceActivity;

    public NotificationAdapter(Notifications notifications, FragmentActivity sourceActivity) {
        Log.d("seanTag", "Construct adaptor");
        this.notifications = notifications;
        this.sourceActivity = sourceActivity;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_item_list, viewGroup, false);
        NotificationAdapter.NotificationViewHolder newNotificationViewHolder = new NotificationAdapter.NotificationViewHolder(view);

        Log.d("seanTag", "Create");

        return newNotificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int position) {
        //Get the notifications at the provided position
        final Notification item = notifications.getNotificationAtPosition(position);
        //Index to pass to the edit activity
        final int index = notificationViewHolder.getAdapterPosition();

        //Fill the text fields with the object's notifications
        //bookListingViewHolder.bookThumbnail.//todo apply photo
        notificationViewHolder.notificationBookThumbnail.setImageResource(R.mipmap.ic_launcher);
        notificationViewHolder.notificationBookTitle.setText(item.getRequestedBookListing().getBook().getTitle());
        notificationViewHolder.notificationUsername.setText(item.getUserMakingNotification());
        notificationViewHolder.notificationUserInfo.setText(item.getNotificationType().toString());

        Log.d("seanTag", item.getRequestedBookListing().getBook().getTitle());

        notificationViewHolder.notificationStatus.setText(item.getRequestedBookListing().getStatus().toString());
        notificationViewHolder.item = item;

        //Add the click listener to the item
        /**
         * Click Listener for individual list items. Starts view activity for the clicked item.
         */
        notificationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem(item);
            }
        });
    }

    private void clickedItem(Notification item) {
        //Start View/Edit Activity with Clicked Item
        //Intent intent = new Intent(sourceActivity, ListingViewActivity.class);
        //intent.putExtra("username", item.getRequestedBookListing().getOwnerUsername());
        //intent.putExtra("bookisbn", item.getRequestedBookListing().getBook().getIsbn());
        //sourceActivity.startActivity(intent);
    }

    /**
     * Override to get the number of items in the dataset
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ImageView notificationBookThumbnail;
        private TextView notificationBookTitle;
        private TextView notificationUserInfo;
        private TextView notificationUsername;
        private TextView notificationStatus;
        private Notification item;

        /**
         * Creates the BookListingViewHolder
         *
         * @param notificationView The view for this item
         */
        public NotificationViewHolder(@NonNull View notificationView) {
            super(notificationView);

            //Obtain Layout Object References
            notificationBookThumbnail = notificationView.findViewById(R.id.notificationBookThumbnail);
            notificationBookTitle = notificationView.findViewById(R.id.notificationBookTitle);
            notificationUserInfo = notificationView.findViewById(R.id.notificationUserInfo);
            notificationUsername = notificationView.findViewById(R.id.notificationUsername);
            notificationStatus = notificationView.findViewById(R.id.notificationStatus);
        }
    }
}
