package ar.codeslu.plax;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.farhanahmed.pico.Pico;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.model.Media;
import com.sinch.android.rtc.SinchClient;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.me.UserIn;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ar.codeslu.plax.auth.DataSet;
import ar.codeslu.plax.calls.BaseActivity;
import ar.codeslu.plax.calls.CallingActivity;
import ar.codeslu.plax.calls.CallingActivityVideo;
import ar.codeslu.plax.calls.SinchService;
import ar.codeslu.plax.custom.AttachMenu;
import ar.codeslu.plax.datasetters.MessageData;
import ar.codeslu.plax.fragments.Chats;
import ar.codeslu.plax.global.AppBack;
import ar.codeslu.plax.global.GetTime;
import ar.codeslu.plax.global.Global;
import ar.codeslu.plax.holders.CustomIncomingImageMessageViewHolder;
import ar.codeslu.plax.holders.CustomOutcomingImageMessageViewHolder;
import ar.codeslu.plax.holders.IncomeHolder;
import ar.codeslu.plax.holders.IncomeOther;
import ar.codeslu.plax.holders.OutcomeHolder;
import ar.codeslu.plax.holders.OutcomeOther;
import ar.codeslu.plax.lists.OnlineGetter;
import ar.codeslu.plax.lists.Tokens;
import ar.codeslu.plax.lists.UserData;
import ar.codeslu.plax.lists.myD;
import ar.codeslu.plax.lists.senderD;

import com.stfalcon.chatkit.me.Message;
import com.stfalcon.chatkit.me.MessageIn;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.NormalFile;


import ar.codeslu.plax.notify.FCM;
import ar.codeslu.plax.notify.FCMresp;
import ar.codeslu.plax.notify.Sender;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import im.delight.android.location.SimpleLocation;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.simbio.encryption.Encryption;

import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class Chat extends BaseActivity
        implements
        MessagesListAdapter.OnLoadMoreListener {

    //input
    FirebaseAuth mAuth;
    String friendId = "";
    DatabaseReference type;
    DatabaseReference mData, myData;
    ArrayList<UserData> mylist;
    senderD userData;
    myD data;
    //output
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;
    private static final int TOTAL_MESSAGES_COUNT = 0;
    private int selectionCount;
    private Date lastLoadedDate;
    private MessagesList messagesList;
    DatabaseReference mDataget, mdatagetme;
    MessagesListAdapter.HoldersConfig holdersConfig;
    //view
    RelativeLayout ly;
    Encryption encryption;
    ImageView add, send, emoji, bg;
    private RecordButton voice;
    RecordView recordView;
    EmojiEditText message;
    private LinearLayout messagebox, overdark;
    private boolean isHidden = true;
    ImageButton btnI, btnV, btnVideo, btnF, btnL,btnS;
    private Bitmap compressedImageFile;
    //typing
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 1300;
    boolean typingR, recordingR = false;
    int code = 0;
    //voice record
    private MediaRecorder mRecorder;
    private long recordTime = 0;
    private File mOutputFile;
    //map
    private SimpleLocation location;
    //all
    Object currTime;
    String messidL = "", encrypM = "", encrypI = "", encrypL = "", encrypV = "", encrypF = "", encrypVideo = "", encrypMap = "";
    //Types id
    byte Voiceid = 2;
    //toolbar
    Toolbar toolbar;
    CircleImageView ava;
    ImageView callA, callV;
    EmojiTextView name, state;
    //check online
    DatabaseReference mUserDB;
    //shared pref
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //fcm
    FCM fcm;
    //thumb
    Bitmap thumb;
    //real time
    Handler h = new Handler();
    int TIMEUPDATE = 60 * 1000;
    Runnable runnable;
    //exist
    ImageView close;
    Button block, addcontact;
    LinearLayout existlay;
    ImageView imm, downdown;
    //get Message query
    Query query;
    //local message
    MessageIn messageLocal;
    //data query lists
    ArrayList<String> fileA, imageA;
    int fq = 0, iq = 0;
    boolean fqb = true, iqb = true;
    //dialog
    AttachMenu cdd;
    CircleImageView attachmenuP;
    LinearLayout reactD;
    //screenshot
    boolean screen = true;
    //record delete on pause
    private boolean pausebreak = false;
    Boolean canScroll = false;
    //vars
    String filetype = "";
    //call
    com.sinch.android.rtc.calling.Call call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global.chatactivity = this;
        Global.audiolist = new ArrayList<>();
        Global.btnid = new ArrayList<>();
        if (getIntent() != null) {
            Intent intent = getIntent();
            Global.currscreen = intent.getExtras().getBoolean("screen");
            Global.myscreen = ((AppBack) getApplication()).shared().getBoolean("screenP", false);
            if (Global.myscreen && Global.currscreen)
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_chat);
        Global.conA = this;
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference(Global.CHATS);
        type = FirebaseDatabase.getInstance().getReference(Global.CHATS);
        mDataget = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mdatagetme = FirebaseDatabase.getInstance().getReference(Global.USERS);
        mUserDB = FirebaseDatabase.getInstance().getReference().child(Global.USERS);
        //init Arrays
        fileA = new ArrayList<>();
        imageA = new ArrayList<>();
        //local db
        ly = findViewById(R.id.lyC);
        add = findViewById(R.id.attachmentButton);
        voice = findViewById(R.id.voice);
        send = findViewById(R.id.send);
        imm = findViewById(R.id.imm);
        bg = findViewById(R.id.bg);
        emoji = findViewById(R.id.emoji);
        messagebox = findViewById(R.id.messagebox);
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        recordView = (RecordView) findViewById(R.id.record_view);
        message = findViewById(R.id.messageInput);
        overdark = findViewById(R.id.overdark);
        //exist
        block = findViewById(R.id.block);
        addcontact = findViewById(R.id.addcontact);
        close = findViewById(R.id.close);
        existlay = findViewById(R.id.notcontact);
        existlay.setVisibility(View.GONE);
        downdown = findViewById(R.id.downdown);
        downdown.setVisibility(View.GONE);
        overdark.setVisibility(View.GONE);

//set Wallpapers
        if (!((AppBack) getApplication()).shared().getString("wall", "no").equals("no")) {
            String pathW = ((AppBack) getApplication()).shared().getString("wall", "no");
            File f = new File(getRealPathFromURI(Uri.parse(pathW)));
            Drawable d = Drawable.createFromPath(f.getAbsolutePath());
            ly.setBackground(d);
        }
        //clear all notifications
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            if (notificationManager != null) {
                notificationManager.cancelAll();
                int count = 0;
                //store it again
                ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                ((AppBack) getApplication()).editSharePrefs().apply();
                ShortcutBadger.applyCount(this, count);
            }
        } catch (NullPointerException e) {
            //nothing
        }

        //attach mnu init
        cdd = new AttachMenu(this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        cdd.show();
        //menu inti
        btnI = cdd.findViewById(R.id.gallery);
        btnV = cdd.findViewById(R.id.audio);
        btnS = cdd.findViewById(R.id.snap);
        btnL = cdd.findViewById(R.id.location);
        btnVideo = cdd.findViewById(R.id.video);
        btnF = cdd.findViewById(R.id.file);
        attachmenuP = cdd.findViewById(R.id.profileAttach);
        reactD = cdd.findViewById(R.id.reactD);
        cdd.dismiss();
        isHidden = true;


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int imagewidth = (int) Math.round(displaymetrics.widthPixels * 0.99);
        int imageheight = (int) Math.round(displaymetrics.heightPixels);

        //dark mode init
        if (mAuth.getCurrentUser() != null) {
            if (!((AppBack) getApplication()).shared().getBoolean("dark" + mAuth.getCurrentUser().getUid(), false)) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                message.setBackground(getResources().getDrawable(R.drawable.round_w));
                messagebox.setBackground(getResources().getDrawable(R.drawable.round_w));
                message.setTextColor(getResources().getColor(R.color.black));
                recordView.setBackground(getResources().getDrawable(R.drawable.round_w));
                add.setBackground(getResources().getDrawable(R.drawable.circle));
                downdown.setBackground(getResources().getDrawable(R.drawable.circle));
                imm.setBackground(getResources().getDrawable(R.drawable.circle));
                reactD.setBackground(getResources().getDrawable(R.drawable.react_bg));
                Global.DARKSTATE = false;
//wallpaper
                overdark.setVisibility(View.GONE);
                if (((AppBack) getApplication()).shared().getString("wall", "no").equals("no"))
                    bg.setImageResource(R.drawable.bg2);


                else {
                    Uri pathW = Uri.parse(((AppBack) getApplication()).shared().getString("wall", "no"));
                    File f = new File(getRealPathFromURI(pathW));
                    if (f.exists()) {
                        Picasso.get()
                                .load(pathW)
                                .resize(imagewidth,imageheight)
                                .error(R.drawable.bg2)
                                .into(bg);

                    } else
                        bg.setImageResource(R.drawable.bg2);


                }
            } else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                message.setBackground(getResources().getDrawable(R.drawable.round_d));
                messagebox.setBackground(getResources().getDrawable(R.drawable.round_d));
                message.setTextColor(getResources().getColor(R.color.white));
                recordView.setBackground(getResources().getDrawable(R.drawable.round_d));
                add.setBackground(getResources().getDrawable(R.drawable.circle_d));
                downdown.setBackground(getResources().getDrawable(R.drawable.circle_d));
                imm.setBackground(getResources().getDrawable(R.drawable.circle_d));
                reactD.setBackground(getResources().getDrawable(R.drawable.react_bg_d));
                Global.DARKSTATE = true;
                //wallpaper
                overdark.setVisibility(View.VISIBLE);
                if (((AppBack) getApplication()).shared().getString("wall", "no").equals("no")) {
                    bg.setImageResource(R.drawable.bg3);
                    overdark.setVisibility(View.GONE);
                } else {
                    Uri pathW = Uri.parse(((AppBack) getApplication()).shared().getString("wall", "no"));
                    File f = new File(getRealPathFromURI(pathW));
                    if (f.exists()) {
                        Picasso.get()
                                .load(pathW)
                                .resize(imagewidth,imageheight)
                                .error(R.drawable.bg3)
                                .into(bg);
                    } else {
                        bg.setImageResource(R.drawable.bg3);
                        overdark.setVisibility(View.GONE);
                    }


                }
            }
        }

        //location
        location = new SimpleLocation(this);
        //fcm notify
        fcm = Global.getFCMservies();
        Global.currentactivity = this;
        //online checker
        ((AppBack) this.getApplication()).startOnline();
//toolbar
        //Actionbar init
        toolbar = (Toolbar) findViewById(R.id.chatbar);
        toolbar.setPadding(0, 0, 0, 0);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        //Action bar design
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewS = inflater.inflate(R.layout.chat_bar, null);
        actionBar.setCustomView(viewS);
        name = viewS.findViewById(R.id.nameC);
        state = viewS.findViewById(R.id.stateC);
        ava = viewS.findViewById(R.id.avaC);
        callA = viewS.findViewById(R.id.callAC);
        callV = viewS.findViewById(R.id.callVC);
//emoji keyboard
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(ly).build(message);

//downloader
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
        //encryption
        byte[] iv = new byte[16];
        encryption = Encryption.getDefault(Global.keyE, Global.salt, iv);
        voice.setRecordView(recordView);
        if (getIntent() != null) {
            Intent intent = getIntent();
            friendId = intent.getExtras().getString("id");
            Global.currentpageid = friendId;
            Query query = mDataget.child(friendId);
            query.keepSynced(true);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userData = dataSnapshot.getValue(senderD.class);
                    Global.currname = userData.getName();
                    Global.currAva = userData.getAvatar();
                    Global.onstate = userData.isOnline();
                    Global.currtime = userData.getTime();
                    Global.currstatue = userData.getStatue();
                    Global.currphone = userData.getPhone();
                    Global.currscreen = userData.isScreen();
                    editInf();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            code = intent.getExtras().getInt("ccode");
            //number for checking the contact
            Global.currphone = intent.getExtras().getString("phone");
        }
        //save message
        preferences = getSharedPreferences("messagebox", Context.MODE_PRIVATE);
        editor = preferences.edit();
//retrive message
        SharedPreferences preferences = getSharedPreferences("messagebox", Context.MODE_PRIVATE);
        message.setText(preferences.getString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), ""));
        //toolbar data get

            if (getIntent() != null) {
                Intent intent = getIntent();
                friendId = intent.getExtras().getString("id");
                Global.currname = intent.getExtras().getString("name");
                Global.currAva = intent.getExtras().getString("ava");
                Global.currphone = intent.getExtras().getString("phone");
                Global.currscreen = intent.getExtras().getBoolean("screen");
            }
            name.setText(Global.currname);
            if (String.valueOf(Global.currAva).equals("no")) {
                Picasso.get()
                        .load(R.drawable.profile)
                        .error(R.drawable.errorimg)
                        .into(ava);
            } else {
                Picasso.get()
                        .load(Global.currAva)
                        .error(R.drawable.errorimg)
                        .into(ava);
            }
            state.setVisibility(View.GONE);

        Map<String,Object> mapp = new HashMap<>();
        mapp.put("typing",false);
        mapp.put("audio",false);
        type.child(mAuth.getCurrentUser().getUid()).child(friendId).onDisconnect().updateChildren(mapp);
//typing

        Global.currFid = friendId;
        Query query = type.child(friendId).child(mAuth.getCurrentUser().getUid());
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserIn userIn = dataSnapshot.getValue(UserIn.class);
                if (userIn != null) {
                    typingR = userIn.isTyping();
                    recordingR = userIn.isAudio();
                    typingit();
                } else {
                    typingR = false;
                    recordingR = false;
                    typingit();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mylist = new ArrayList<>();
        myData = FirebaseDatabase.getInstance().getReference(Global.USERS);
        Query query1 = myData.child(mAuth.getCurrentUser().getUid());
        query1.keepSynced(true);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(myD.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Query query2 = mdatagetme.child(mAuth.getCurrentUser().getUid());
        query2.keepSynced(true);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myD userData = dataSnapshot.getValue(myD.class);
                Global.nameLocal = userData.getName();
                Global.avaLocal = userData.getAvatar();
                Global.myscreen = userData.isScreen();
                Global.myonstate = userData.isOnline();
                //attach menu photo
                if (String.valueOf(Global.avaLocal).equals("no")) {
                    Picasso.get()
                            .load(R.drawable.profile)
                            .error(R.drawable.errorimg)
                            .into(attachmenuP);
                } else {
                    Picasso.get()
                            .load(Global.avaLocal)
                            .error(R.drawable.errorimg)
                            .into(attachmenuP);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        downdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downdown.setVisibility(View.GONE);
                messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                canScroll = false;
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            messagesList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (!messagesList.canScrollVertically(5)) {
                        canScroll = false;
                        downdown.setVisibility(View.GONE);
                    } else {
                        canScroll = true;
                        downdown.setVisibility(View.VISIBLE);

                    }

                }
            });
        } else {
            messagesList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!messagesList.canScrollVertically(1)) {
                        canScroll = false;
                        downdown.setVisibility(View.GONE);
                    } else {
                        canScroll = true;
                        downdown.setVisibility(View.VISIBLE);

                    }

                }
            });
        }

        if (code == 0) {
            zeroCount();
            readM();
        }
        //output
        imageLoader = new ImageLoader() {

            @Override
            public void loadImage(final ImageView imageView, final String url, Object payload) {
                if (String.valueOf(url).equals("no")) {
                    Picasso.get()
                            .load(R.drawable.profile)
                            .error(R.drawable.errorimg)
                            .into(imageView);
                } else {
                    Picasso.get()
                            .load(url)
                            .placeholder(getResources().getDrawable(R.drawable.loading))
                            .error(R.drawable.errorimg)
                            .into(imageView);
                }
            }
        };

        //get messages
        getChats();
        //typing
        if (message.getText().toString().isEmpty()) {
            voice.setVisibility(View.VISIBLE);
            send.setEnabled(false);
            send.setVisibility(View.GONE);
            imm.setVisibility(View.VISIBLE);
        } else {
            voice.setVisibility(View.GONE);
            send.setEnabled(true);
            send.setVisibility(View.VISIBLE);
            imm.setVisibility(View.GONE);
        }
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Global.messG.size() > 0 && Global.messG != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("typing", true);
                    if (Global.messG != null && Global.messG.size() != 0)
                        type.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map);
                    stopTT();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                startTT();
                if (!TextUtils.isEmpty(editable.toString())) {
                    voice.setVisibility(View.GONE);
                    send.setEnabled(true);
                    send.setVisibility(View.VISIBLE);
                    imm.setVisibility(View.GONE);
                } else {
                    voice.setVisibility(View.VISIBLE);
                    send.setEnabled(false);
                    send.setVisibility(View.GONE);
                    imm.setVisibility(View.VISIBLE);
                }
                //resize message box
                getSize();
                editor.putString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), message.getText().toString());
                editor.apply();

            }
        });
        final String[] input = new String[1];


        //send message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input[0] = message.getText().toString();
                if (!input[0].trim().isEmpty()) {
                    message.setText("");
                    getSize();
                    editor.putString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), message.getText().toString());
                    editor.apply();
                    if (emojiPopup.isShowing()) {
                        emoji.setImageResource(R.drawable.ic_emoji);
                        emojiPopup.dismiss();
                    }
                    Global.yourM = false;
                    encrypM = String.valueOf(input[0]).trim();
                    encrypM = encryption.encryptOrNull(encrypM);
                    currTime = ServerValue.TIMESTAMP;
                    messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("avatar",Global.avaLocal);
                    map.put("name",Global.nameLocal);
                    map.put("nameL",Global.nameLocal);
                    map.put("phone",Global.phoneLocal);
                    map.put("id", mAuth.getCurrentUser().getUid());
                    map.put("screen", Global.myscreen);
                    map.put("lastmessage", encrypM);
                    map.put("lastsender", mAuth.getCurrentUser().getUid());
                    map.put("lastsenderava",Global.avaLocal);
                    map.put("messDate", currTime);

                    //local message
                    messagesAdapter.clear();
                    messageLocal = new MessageIn(encrypM, "text", "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "no",encryption.encryptOrNull(Global.avaLocal),true);
                    try {
                        Global.messG.add(messageLocal);
                        //local store
                        ((AppBack) getApplication()).setchatsdb(friendId);
                    } catch (NullPointerException e) {
                        Global.messG = new ArrayList<>();
                        Global.messG.add(messageLocal);
                        //local store
                        ((AppBack) getApplication()).setchatsdb(friendId);
                    }

                    //update last message if dialog exist
                    Chats chat = new Chats();
                    //update dialog if not exist
                    UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, messageLocal.getMessage(), mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0, Global.currscreen);
                    ArrayList<UserIn> tempoo = new ArrayList<>();
                    tempoo.clear();
                    tempoo.add(dialog);
                    Global.userrG = dialog;
                    Global.Dialogonelist = tempoo;
                    Global.Dialogid = friendId;
                    Global.DialogM = messageLocal;
                    chat.onNewMessage();


                    messagesAdapter.addToEnd(MessageData.getMessages(), true);
                    messagesAdapter.notifyDataSetChanged();
                    messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                    ///////

                    mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendM();
                        }
                    });
                } else
                    Snackbar.make(ly, R.string.empty_mess, Snackbar.LENGTH_SHORT).show();
            }
        });

        message.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (((AppBack) getApplication()).shared().getBoolean("enter", false)) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                input[0] = message.getText().toString();
                                if (!input[0].trim().isEmpty()) {
                                    message.setText("");
                                    getSize();
                                    editor.putString("chatM_" + friendId + "_" + mAuth.getCurrentUser().getUid(), message.getText().toString());
                                    editor.apply();
                                    if (emojiPopup.isShowing()) {
                                        emoji.setImageResource(R.drawable.ic_emoji);
                                        emojiPopup.dismiss();
                                    }
                                    Global.yourM = false;
                                    encrypM = String.valueOf(input[0]).trim();
                                    encrypM = encryption.encryptOrNull(encrypM);
                                    currTime = ServerValue.TIMESTAMP;
                                    messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                                    //send owner data to friend
                                    mAuth = FirebaseAuth.getInstance();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("avatar",Global.avaLocal);
                                    map.put("name",Global.nameLocal);
                                    map.put("nameL",Global.nameLocal);
                                    map.put("phone",Global.phoneLocal);
                                    map.put("id", mAuth.getCurrentUser().getUid());
                                    map.put("screen", Global.myscreen);
                                    map.put("lastmessage", encrypM);
                                    map.put("lastsender", mAuth.getCurrentUser().getUid());
                                    map.put("lastsenderava",Global.avaLocal);
                                    map.put("messDate", currTime);

                                    //local message
                                    messagesAdapter.clear();
                                    messageLocal = new MessageIn(encrypM, "text", "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "no",encryption.encryptOrNull(Global.avaLocal),true);
                                    try {
                                        Global.messG.add(messageLocal);
                                        //local store
                                        ((AppBack) getApplication()).setchatsdb(friendId);
                                    } catch (NullPointerException e) {
                                        Global.messG = new ArrayList<>();
                                        Global.messG.add(messageLocal);
                                        //local store
                                        ((AppBack) getApplication()).setchatsdb(friendId);
                                    }

                                    //update last message if dialog exist
                                    Chats chat = new Chats();
                                    //update dialog if not exist
                                    UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, messageLocal.getMessage(), mAuth.getCurrentUser().getUid(), Global.avaLocal, messageLocal.getTime(), 0, Global.currscreen);
                                    ArrayList<UserIn> tempoo = new ArrayList<>();
                                    tempoo.clear();
                                    tempoo.add(dialog);
                                    Global.userrG = dialog;
                                    Global.Dialogonelist = tempoo;
                                    Global.Dialogid = friendId;
                                    Global.DialogM = messageLocal;
                                    chat.onNewMessage();


                                    messagesAdapter.addToEnd(MessageData.getMessages(), true);
                                    messagesAdapter.notifyDataSetChanged();
                                    messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                                    ///////

                                    mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            sendM();
                                        }
                                    });
                                } else
                                    Snackbar.make(ly, R.string.empty_mess, Snackbar.LENGTH_SHORT).show();
                                return true;
                            default:
                                break;
                        }
                    }
                }
                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHidden) {
                    cdd.show();
                    isHidden = false;
                } else {
                    cdd.dismiss();
                    isHidden = true;
                }

            }
        });
        btnI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iqb) {
                    cdd.dismiss();
                    isHidden = true;
                    uploadI();
                } else
                    Toast.makeText(Chat.this, R.string.wait, Toast.LENGTH_SHORT).show();
            }
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fqb) {
                    cdd.dismiss();
                    isHidden = true;
                    Intent intent4 = new Intent(Chat.this, NormalFilePickActivity.class);
                    intent4.putExtra(Constant.MAX_NUMBER, Global.fileS);
                    intent4.putExtra(IS_NEED_FOLDER_LIST, true);
                    intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf", "txt"});
                    startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
                } else
                    Toast.makeText(Chat.this, R.string.wait, Toast.LENGTH_SHORT).show();
            }

        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;

                Pico.openMultipleFiles(Chat.this, Pico.TYPE_VIDEO);

            }
        });
        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                Intent intent3 = new Intent(Chat.this, AudioPickActivity.class);
                intent3.putExtra(IS_NEED_RECORDER, true);
                intent3.putExtra(IS_NEED_FOLDER_LIST, true);
                intent3.putExtra(Constant.MAX_NUMBER, Global.audioS);
                startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
            }
        });
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                SandriosCamera
                        .with()
                        .setShowPicker(true)
                        .setMediaAction(CameraConfiguration.MEDIA_QUALITY_MEDIUM)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO)
                        .enableImageCropping(true)
                        .launchCamera(Chat.this);
            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) {
                    emoji.setImageResource(R.drawable.ic_emoji);
                    emojiPopup.dismiss();
                } else {
                    emoji.setImageResource(R.drawable.ic_keyboard);
                    emojiPopup.toggle();
                }
            }
        });
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cdd.dismiss();
                isHidden = true;

                Dexter.withActivity(Chat.this)
                        .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    // if we can't access the location yet
                                    if (!location.hasLocationEnabled()) {
                                        // ask the user to enable location access
                                        SimpleLocation.openSettings(Chat.this);
                                    } else {
                                        encrypL = location.getLatitude() + "," + location.getLongitude();
                                        encrypL = encryption.encryptOrNull(encrypL);
                                        encrypMap = getString(R.string.map_location);
                                        encrypMap = encryption.encryptOrNull(encrypMap);
                                        currTime = ServerValue.TIMESTAMP;
                                        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                                        //send owner data to friend
                                        mAuth = FirebaseAuth.getInstance();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("avatar",Global.avaLocal);
                                        map.put("name",Global.nameLocal);
                                        map.put("nameL",Global.nameLocal);
                                        map.put("phone",Global.phoneLocal);
                                        map.put("id", mAuth.getCurrentUser().getUid());
                                        map.put("screen", Global.myscreen);
                                        map.put("lastmessage", encrypMap);
                                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                                        map.put("lastsenderava",Global.avaLocal);
                                        map.put("messDate", currTime);
                                        //local message
                                        messagesAdapter.clear();
                                        messageLocal = new MessageIn(encrypL, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, "no", false, messidL, "map",encryption.encryptOrNull(Global.avaLocal),true);
                                        try {
                                            Global.messG.add(messageLocal);
                                            //local store
                                            ((AppBack) getApplication()).setchatsdb(friendId);
                                        } catch (NullPointerException e) {
                                            Global.messG = new ArrayList<>();
                                            Global.messG.add(messageLocal);
                                            //local store
                                            ((AppBack) getApplication()).setchatsdb(friendId);
                                        }
                                        //update last message if dialog exist
                                        Chats chat = new Chats();
                                        //update dialog if not exist
                                        UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, encrypMap, mAuth.getCurrentUser().getUid(), Global.avaLocal, System.currentTimeMillis(), 0, Global.currscreen);
                                        ArrayList<UserIn> tempoo = new ArrayList<>();
                                        tempoo.clear();
                                        tempoo.add(dialog);
                                        Global.userrG = dialog;
                                        Global.yourM = false;
                                        Global.Dialogonelist = tempoo;
                                        Global.Dialogid = friendId;
                                        Global.DialogM = messageLocal;
                                        chat.onNewMessage();
                                        messagesAdapter.addToEnd(MessageData.getMessages(), true);
                                        messagesAdapter.notifyDataSetChanged();
                                        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                                        ///////
                                        mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendMap();
                                            }
                                        });
                                    }
                                } else
                                    Toast.makeText(Chat.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        }).check();


            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdd.dismiss();
                isHidden = true;
                if (emojiPopup.isShowing()) {
                    emoji.setImageResource(R.drawable.ic_emoji);
                    emojiPopup.dismiss();
                }

            }
        });

//voice record
        recordView.setVisibility(View.GONE);
        messagebox.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {

                Dexter.withActivity(Chat.this)
                        .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {
                                    //Start Recording..
                                    recordView.setVisibility(View.VISIBLE);
                                    messagebox.setVisibility(View.GONE);
                                    add.setVisibility(View.GONE);
                                    startRecording();
                                } else
                                    Toast.makeText(Chat.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                token.continuePermissionRequest();

                            }
                        }).check();

            }

            @Override
            public void onCancel() {
                try {
                    stopRecording(false);
                } catch (RuntimeException e) {
                    mOutputFile.delete();
                    stopAT();
                }
                setResult(RESULT_CANCELED);
            }

            @Override
            public void onFinish(long recordTime) {
                recordView.setVisibility(View.GONE);
                messagebox.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                if (!pausebreak) {
                    try {
                        stopRecording(true);
                        Uri uri = Uri.parse("file://" + mOutputFile.getAbsolutePath());
                        setResult(Activity.RESULT_OK, new Intent().setData(uri));
                        Log.wtf("keyyy","1122");

                        uploadV(uri, recordTime);
                    } catch (NullPointerException e) {
                        stopAT();

                    }
                }
            }

            @Override
            public void onLessThanSecond() {
                try {
                    stopAT();
                    recordView.setVisibility(View.GONE);
                    messagebox.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    try {
                        stopRecording(false);
                    } catch (RuntimeException e) {
                        mOutputFile.delete();
                        stopAT();
                    }
                    setResult(RESULT_CANCELED);
                } catch (NullPointerException e) {
                    stopAT();
                }

            }
        });
        voice.setListenForRecord(true);
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                recordView.setVisibility(View.GONE);
                messagebox.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
            }
        });
        //get realtime time
        h.postDelayed(runnable = new Runnable() {
            public void run() {
                if (!Global.onstate)
                    state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));
                h.postDelayed(runnable, TIMEUPDATE);
            }
        }, TIMEUPDATE);
        //all exist layout elements
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //something
            }
        });
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, Global.currphone);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, Global.currname);
                startActivity(addContactIntent);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existlay.setVisibility(View.GONE);
                editor.putBoolean("close_" + friendId + "_" + mAuth.getCurrentUser().getUid(), true);
                editor.apply();
            }
        });
//check contact exist
        Dexter.withActivity(Chat.this)
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted())
                            contactExists(Global.currphone);

                        else
                            Toast.makeText(Chat.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();


        imm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iqb) {
                    uploadI();
                } else
                    Toast.makeText(Chat.this, R.string.wait, Toast.LENGTH_SHORT).show();

            }
        });

        callA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   ((AppBack) getApplication()).callUserA(friendId);
                Intent jumptocall= new Intent(Chat.this, CallingActivity.class);

                jumptocall.putExtra("Userid", friendId);
                startActivity(jumptocall);
            }
        });

        callV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((AppBack) getApplication()).callUserV(friendId);
                call = getSinchServiceInterface().callUserVideo(friendId);
                String callId = call.getCallId();
                Intent jumptocall= new Intent(Chat.this, CallingActivityVideo.class);
                jumptocall.putExtra(SinchService.CALL_ID, callId);
                jumptocall.putExtra("UserId", friendId);
                startActivity(jumptocall);
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            stopRecording(false);
        } catch (RuntimeException e) {
        }
        setResult(RESULT_CANCELED);
        Global.currentpageid = "";
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }


    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
                ArrayList<Message> messages = MessageData.getMessages();
//                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
    }

    private void initAdapter() {
        //todo
        holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setIncoming(IncomeHolder.class, R.layout.item_incoming_text_message);
        holdersConfig.setOutcoming(OutcomeHolder.class, R.layout.item_outcoming_text_message);
        holdersConfig.setIncomingImageHolder(CustomIncomingImageMessageViewHolder.class, R.layout.item_incoming_image_message);
        holdersConfig.setOutcomingImageHolder(CustomOutcomingImageMessageViewHolder.class, R.layout.item_outcoming_image_message);
        holdersConfig.setDateHeaderLayout(R.layout.item_date_header);
        holdersConfig.registerContentType(Voiceid, IncomeOther.class, R.layout.item_incoming_other_message,
                OutcomeOther.class, R.layout.item_outcoming_other_message, new MessageHolders.ContentChecker() {
                    @Override
                    public boolean hasContentFor(Message message, byte type) {
                        if (type == 2) {
                            try {
                                return message.getVoice().getUrl() != null;
                            } catch (NullPointerException e) {
                                try {
                                    return message.getFile().getUrl() != null;

                                } catch (NullPointerException e1) {
                                    try {
                                        return message.getVideo().getUrl() != null;

                                    } catch (NullPointerException e2) {
                                        try {
                                            return message.getMap().getLocation() != null;

                                        } catch (NullPointerException e3) {
                                            return false;

                                        }
                                    }

                                }

                            }
                        } else
                            return false;
                    }
                });
        messagesAdapter = new MessagesListAdapter<>(mAuth.getCurrentUser().getUid(), holdersConfig, imageLoader);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(new DateFormatter.Formatter() {
            @Override
            public String format(Date date) {
                if (DateFormatter.isToday(date)) {
                    return getString(R.string.date_header_today);
                } else if (DateFormatter.isYesterday(date)) {
                    return getString(R.string.date_header_yesterday);
                } else {
                    return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
                }


            }
        });
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<Message>() {
                    @Override
                    public void onMessageViewClick(View view, Message message) {
//todo
                    }
                });
        messagesList.setAdapter(messagesAdapter);
        messagesList.setNestedScrollingEnabled(true);

    }

    private void sendM() {
        final Map<String, Object> map = new HashMap<>();
        map.put("message", encrypM);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", true);
        map.put("seen", false);
        map.put("type", "text");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("from", mAuth.getCurrentUser().getUid());
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages).child(messidL)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //send friend data to owner
                        mAuth = FirebaseAuth.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("avatar",Global.currAva);
                        map.put("name",Global.currname);
                        map.put("nameL",Global.currname);
                        map.put("phone",Global.currphone);
                        map.put("screen", Global.currscreen);
                        map.put("lastmessage", encrypM);
                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                        map.put("lastsenderava",Global.avaLocal);
                        map.put("messDate", currTime);
                        map.put("id", friendId);
                        //  ------------
                        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypM, messidL);

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("location", encrypL);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", true);
        map.put("seen", false);
        map.put("type", "map");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("from", mAuth.getCurrentUser().getUid());
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages).child(messidL)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //send friend data to owner
                        mAuth = FirebaseAuth.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("avatar",Global.currAva);
                        map.put("name",Global.currname);
                        map.put("nameL",Global.currname);
                        map.put("phone",Global.currphone);
                        map.put("screen", Global.currscreen);
                        map.put("lastmessage", encrypMap);
                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                        map.put("lastsenderava",Global.avaLocal);
                        map.put("messDate", currTime);
                        map.put("id", friendId);
                        //  ------------
                        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypMap, messidL);

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

//    private void checkFailedMessage() {
//        runOnUiThread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }
//        );
//    }

    private void getChats() {

        final long[] keyOnce = {0};
        final int[] i = {0};
        final int[] onceOnce = {0};
        //getride of old data
        ((AppBack) getApplication()).getchatsdb(friendId);
        query = mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).orderByChild("time");
        query.keepSynced(true);
        initAdapter();
        //just init because of the first time offline started chat
        messagesAdapter.clear();
        messagesAdapter.addToEnd(MessageData.getMessages(), true);
        messagesAdapter.notifyDataSetChanged();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                keyOnce[0] = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(!Global.check_int(Chat.this))
        {
            ((AppBack) getApplication()).getchatsdb(friendId);
            //update the list
            messagesAdapter.clear();
            messagesAdapter.addToEnd(MessageData.getMessages(), true);
            messagesAdapter.notifyDataSetChanged();
        }
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    if (Global.check_int(Chat.this)) {
                        MessageIn message = dataSnapshot.getValue(MessageIn.class);

                        if (messagesAdapter.halbine(Global.messG, message.getMessId()) == -1)
                            Global.messG.add(message);
                        else
                            Global.messG.set(messagesAdapter.halbine(Global.messG, message.getMessId()), message);

                        if (!message.getFrom().equals(mAuth.getCurrentUser().getUid()) && canScroll) {
                            downdown.setVisibility(View.VISIBLE);
                        }

                    }
                    //check only in global list range
                    if (i[0] >= keyOnce[0] - 1) {
                        if (Global.check_int(Chat.this)) {
                            //local store
                            ((AppBack) getApplication()).setchatsdb(friendId);
                        }

                                //update the list
                                messagesAdapter.clear();
                                messagesAdapter.addToEnd(MessageData.getMessages(), true);
                                messagesAdapter.notifyDataSetChanged();
                        messagesList.scrollBy(0,0);



                        if (onceOnce[0] == 0) {
                            keyOnce[0]++;
                        }
                    }

                    i[0]++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MessageIn message = dataSnapshot.getValue(MessageIn.class);
                if(messagesAdapter.halbine(Global.messG, message.getMessId()) != -1)
                Global.messG.set(messagesAdapter.halbine(Global.messG, message.getMessId()), message);

                //local store
                ((AppBack) getApplication()).setchatsdb(friendId);

                        messagesAdapter.clear();
                        messagesAdapter.addToEnd(MessageData.getMessages(), true);
                        messagesAdapter.notifyDataSetChanged();
                        messagesList.scrollBy(0,0);




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                MessageIn message = dataSnapshot.getValue(MessageIn.class);

                if (messagesAdapter.halbine(Global.messG, message.getMessId()) != -1)
                    Global.messG.remove(messagesAdapter.halbine(Global.messG, message.getMessId()));

                //local store
                ((AppBack) getApplication()).setchatsdb(friendId);

                        messagesAdapter.clear();
                        messagesAdapter.addToEnd(MessageData.getMessages(), true);
                        messagesAdapter.notifyDataSetChanged();
                messagesList.scrollBy(0,0);



            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void readM() {
        Query query33 =  mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages);
        query33.keepSynced(true);
        query33.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OnlineGetter getter = ds.getValue(OnlineGetter.class);
                    try {
                        if (getter != null && getter.getStatue().equals("D✔"))
                            ds.child("statue").getRef().setValue("R✔");
                    } catch (NullPointerException e) {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void zeroCount() {

        Map<String, Object> map = new HashMap<>();
        map.put("noOfUnread", 0);
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void startTT() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                if (Global.messG.size() > 0 && Global.messG != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("typing", false);
                    if (Global.messG != null && Global.messG.size() != 0)
                        type.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map);
                }
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }


    public void stopTT() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

    }

    //stop and begin (recording audio)
    public void startAT() {
        if (Global.messG.size() > 0 && Global.messG != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("audio", true);
            if (Global.messG != null && Global.messG.size() != 0)
                type.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map);
        }
    }

    public void stopAT() {
        if (Global.messG.size() > 0 && Global.messG != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("audio", false);
            if (Global.messG != null && Global.messG.size() != 0)
                type.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map);
        }
    }

    public void uploadI() {

        Options options = Options.init()
                .setRequestCode(100)                                                 //Request code for activity results
                .setCount(Global.photoS)                                                         //Number of images to restict selection count
                .setFrontfacing(false)                                                //Front Facing camera on start
                .setImageQuality(ImageQuality.REGULAR)                                  //Image Quality
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);        //Orientaion

        Pix.start(Chat.this, options);


    }

    public void uploadF(Uri linkL, final String filename, String filett) {
        //for local
        String fileL = filename;

        Dexter.withActivity(Chat.this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            filename.replace(" ", "_");
                            if (filett.contains("/xlsx")) {
                                filetype = ".xlsx";
                            } else if (filett.contains("/xls")) {
                                filetype = ".xls";

                            } else if (filett.contains("/docx")) {
                                filetype = ".docx";

                            } else if (filett.contains("/doc")) {
                                filetype = ".doc";

                            } else if (filett.contains("/pptx")) {
                                filetype = ".pptx";
                            } else if (filett.contains("/ppt")) {
                                filetype = ".ppt";

                            } else if (filett.contains("/pdf")) {
                                filetype = ".pdf";

                            } else if (filett.contains("/txt")) {
                                filetype = ".txt";
                            } else if (filett.contains("/plain")) {
                                filetype = ".txt";
                            } else if (filett.contains("/binary")) {
                                filetype = "";
                            } else {
                                filetype = filett;
                            }
                            //local message
                            messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
                            fileA.add(messidL);
                            String locall = encryption.encryptOrNull(String.valueOf(linkL));
                            messagesAdapter.clear();
                            messageLocal = new MessageIn(locall, "..", System.currentTimeMillis(), false, false, messidL, "file", fileL + filetype, mAuth.getCurrentUser().getUid(), "no",encryption.encryptOrNull(Global.avaLocal),true);
                            try {
                                Global.messG.add(messageLocal);
                                //local store
                                ((AppBack) getApplication()).setchatsdb(friendId);
                            } catch (NullPointerException e) {
                                Global.messG = new ArrayList<>();
                                Global.messG.add(messageLocal);
                                //local store
                                ((AppBack) getApplication()).setchatsdb(friendId);
                            }
                            //update last message if dialog exist
                            Chats chat = new Chats();
                            //update dialog if not exist
                            encrypF = "File " + filename + filetype;
                            encrypF = encryption.encryptOrNull(encrypF);
                            UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, encrypF, mAuth.getCurrentUser().getUid(), Global.avaLocal, System.currentTimeMillis(), 0, Global.currscreen);
                            ArrayList<UserIn> tempoo = new ArrayList<>();
                            tempoo.clear();
                            tempoo.add(dialog);
                            Global.userrG = dialog;
                            Global.yourM = false;
                            Global.Dialogonelist = tempoo;
                            Global.Dialogid = friendId;
                            Global.DialogM = messageLocal;
                            chat.onNewMessage();
                            messagesAdapter.addToEnd(MessageData.getMessages(), true);
                            messagesAdapter.notifyDataSetChanged();
                            messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
                            ///////
                            //upload on server
                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference riversRef = mStorageRef.child(Global.Mess + "/" + mAuth.getCurrentUser().getUid() + "/Files/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + filetype);
                            final String finalFiletype = filetype;
                            UploadTask uploadTask = riversRef.putFile(linkL);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return riversRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUrl = task.getResult();
                                        message.setText("");
                                        currTime = ServerValue.TIMESTAMP;
                                        //send owner data to friend
                                        mAuth = FirebaseAuth.getInstance();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("avatar",Global.avaLocal);
                                        map.put("name",Global.nameLocal);
                                        map.put("nameL",Global.nameLocal);
                                        map.put("phone",Global.phoneLocal);
                                        map.put("id", mAuth.getCurrentUser().getUid());
                                        map.put("screen", Global.myscreen);
                                        map.put("lastmessage", encrypF);
                                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                                        map.put("lastsenderava",Global.avaLocal);
                                        map.put("messDate", currTime);
                                        mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                sendFpre(String.valueOf(downloadUrl), filename + finalFiletype);
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                    }
                                }
                            });
                        } else
                            Toast.makeText(Chat.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();


    }

    public void uploadV(Uri linkL, final long time) {
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        String locall = encryption.encryptOrNull(String.valueOf(linkL));
        //local message
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "voice", "no", getHumanTimeText(time),encryption.encryptOrNull(Global.avaLocal),true);
        try {
            Global.messG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdb(friendId);
        } catch (NullPointerException e) {
            Global.messG = new ArrayList<>();
            Global.messG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdb(friendId);
        }
        //update last message if dialog exist
        Chats chat = new Chats();
        //update dialog if not exist
        encrypV = "Voice " + getHumanTimeText(time);
        encrypV = encryption.encryptOrNull(encrypV);
        UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, encrypV, mAuth.getCurrentUser().getUid(), Global.avaLocal, System.currentTimeMillis(), 0, Global.currscreen);
        ArrayList<UserIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.userrG = dialog;
        Global.yourM = false;
        Global.Dialogonelist = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        chat.onNewMessage();
        messagesAdapter.addToEnd(MessageData.getMessages(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
        ///////

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.Mess + "/" + mAuth.getCurrentUser().getUid() + "/Audio/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + ".m4a");
        UploadTask uploadTask = riversRef.putFile(linkL);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    Log.wtf("keyyy",String.valueOf(downloadUrl));
                    try
                    {
                        mOutputFile.delete();

                    }
                    catch (NullPointerException e)
                    {

                    }
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("avatar",Global.avaLocal);
                    map.put("name",Global.nameLocal);
                    map.put("nameL",Global.nameLocal);
                    map.put("phone",Global.phoneLocal);
                    map.put("id", mAuth.getCurrentUser().getUid());
                    map.put("screen", Global.myscreen);
                    map.put("lastmessage", encrypV);
                    map.put("lastsender", mAuth.getCurrentUser().getUid());
                    map.put("lastsenderava",Global.avaLocal);
                    map.put("messDate", currTime);
                    mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.wtf("keyyy","kkkl");

                            sendVpre(String.valueOf(downloadUrl), time);
                        }
                    });

                }
                else
                Log.wtf("keyyy",task.getException().getMessage());

            }
        });

    }

    public void uploadVideo(final Uri linkL, final long time, final String local) {
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        String locall = encryption.encryptOrNull(String.valueOf(linkL));
        //local message
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, messidL, "video", getHumanTimeText(time), "file:///android_asset/loading.jpg", "no",encryption.encryptOrNull(Global.avaLocal),true);
        try {
            Global.messG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdb(friendId);
        } catch (NullPointerException e) {
            Global.messG = new ArrayList<>();
            Global.messG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdb(friendId);
        }
        //update last message if dialog exist
        Chats chat = new Chats();
        //update dialog if not exist
        encrypVideo = "Video " + getHumanTimeText(time);
        encrypVideo = encryption.encryptOrNull(encrypVideo);
        UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, encrypVideo, mAuth.getCurrentUser().getUid(), Global.avaLocal, System.currentTimeMillis(), 0, Global.currscreen);
        ArrayList<UserIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.userrG = dialog;
        Global.yourM = false;
        Global.Dialogonelist = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        chat.onNewMessage();
        messagesAdapter.addToEnd(MessageData.getMessages(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
        ///////
        final String videoidtemp = System.currentTimeMillis() + "";
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.Mess + "/" + mAuth.getCurrentUser().getUid() + "/Video/" + mAuth.getCurrentUser().getUid() + friendId + videoidtemp + ".mp4");
        UploadTask uploadTask = riversRef.putFile(linkL);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("avatar",Global.avaLocal);
                    map.put("name",Global.nameLocal);
                    map.put("nameL",Global.nameLocal);
                    map.put("phone",Global.phoneLocal);
                    map.put("id", mAuth.getCurrentUser().getUid());
                    map.put("screen", Global.myscreen);
                    map.put("lastmessage", encrypVideo);
                    map.put("lastsender", mAuth.getCurrentUser().getUid());
                    map.put("lastsenderava",Global.avaLocal);
                    map.put("messDate", currTime);
                    mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            thumb = ThumbnailUtils.createVideoThumbnail(local, MediaStore.Video.Thumbnails.MINI_KIND);
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            thumb.compress(Bitmap.CompressFormat.PNG, 100, bao);
                            byte[] byteArray = bao.toByteArray();
                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference riversRef = mStorageRef.child(Global.Mess + "/" + mAuth.getCurrentUser().getUid() + "/Video/" + "Thumb/" + mAuth.getCurrentUser().getUid() + friendId + videoidtemp + ".png");
                            UploadTask uploadTask = riversRef.putBytes(byteArray);

                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return riversRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri thumbD = task.getResult();
                                        sendVideopre(String.valueOf(downloadUrl), time, String.valueOf(thumbD));


                                    }
                                }
                            });

                        }
                    });

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    if (Global.check_int(Chat.this)) {
                        iqb = false;
                        imageA.clear();
                        iq = 0;
                    }
                    ArrayList<String> list = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    for (int i = 0; i < list.size(); i++) {
                        String path = list.get(i);
                        afterCompress(path);
                    }
                }
                break;
            case Constant.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                    for (int i = 0; i < list.size(); i++) {
                        uploadV(Uri.parse("file:///" + list.get(i).getPath()), list.get(i).getDuration());
                    }

                }
                break;
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    if (Global.check_int(Chat.this)) {
                        fqb = false;
                        fileA.clear();
                        fq = 0;
                    }
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    for (int i = 0; i < list.size(); i++) {
                        uploadF(Uri.parse("file:///" + list.get(i).getPath()), list.get(i).getName(), list.get(i).getMimeType());
                    }
                }
                break;
        }
        Pico.onActivityResult(this,requestCode,resultCode,data,new Pico.onActivityResultHandler(){

            @Override
            public void onActivityResult(List<File> files) {
                for (File file : files){
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(Chat.this, Uri.fromFile(file));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time );
                    retriever.release();
                    uploadVideo(Uri.parse("file:///" + file.getAbsolutePath()),timeInMillisec, file.getAbsolutePath());

                }
            }

            @Override
            public void onFailure(Exception error) {

            }
        });
        if (resultCode == Activity.RESULT_OK
                && requestCode == SandriosCamera.RESULT_CODE
                && data != null) {
            if (data.getSerializableExtra(SandriosCamera.MEDIA) instanceof Media) {
                Media media = (Media) data.getSerializableExtra(SandriosCamera.MEDIA);
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(Chat.this, Uri.parse(media.getPath()));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time );
                retriever.release();
                uploadVideo(Uri.parse("file:///" + media.getPath()),timeInMillisec, media.getPath());
            }

    }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void afterCompress(String path) {
        //local message
        messidL = mAuth.getCurrentUser().getUid() + "_" + friendId + "_" + String.valueOf(System.currentTimeMillis());
        imageA.add(messidL);
        String locall = encryption.encryptOrNull("file://" + path);
        messagesAdapter.clear();
        messageLocal = new MessageIn(locall, "image", messidL, "..", mAuth.getCurrentUser().getUid(), System.currentTimeMillis(), false, false, "no",encryption.encryptOrNull(Global.avaLocal),true);
        try {
            Global.messG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdb(friendId);
        } catch (NullPointerException e) {
            Global.messG = new ArrayList<>();
            Global.messG.add(messageLocal);
            //local store
            ((AppBack) getApplication()).setchatsdb(friendId);
        }
        //update last message if dialog exist
        Chats chat = new Chats();
        //update dialog if not exist

        encrypI = "Image";
        encrypI = encryption.encryptOrNull(encrypI);

        UserIn dialog = new UserIn(Global.currname, Global.currstatue, Global.currAva, Global.currphone, friendId, encrypI, mAuth.getCurrentUser().getUid(), Global.avaLocal, System.currentTimeMillis(), 0, Global.currscreen);
        ArrayList<UserIn> tempoo = new ArrayList<>();
        tempoo.clear();
        tempoo.add(dialog);
        Global.userrG = dialog;
        Global.yourM = false;
        Global.Dialogonelist = tempoo;
        Global.Dialogid = friendId;
        Global.DialogM = messageLocal;
        chat.onNewMessage();
        messagesAdapter.addToEnd(MessageData.getMessages(), true);
        messagesAdapter.notifyDataSetChanged();
        messagesList.getLayoutManager().smoothScrollToPosition(messagesList, null, 0);
        ///////
        //      //compress the photo
        File newImageFile = new File(path);
        try {
            compressedImageFile = new Compressor(Chat.this)
                    .setMaxHeight(500)
                    .setMaxWidth(500)
                    .setQuality(50)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumbData = baos.toByteArray();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child(Global.Mess + "/" + mAuth.getCurrentUser().getUid() + "/Images/" + mAuth.getCurrentUser().getUid() + friendId + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = riversRef.putBytes(thumbData);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    message.setText("");
                    currTime = ServerValue.TIMESTAMP;
                    //send owner data to friend
                    mAuth = FirebaseAuth.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("avatar",Global.avaLocal);
                    map.put("name",Global.nameLocal);
                    map.put("nameL",Global.nameLocal);
                    map.put("phone",Global.phoneLocal);
                    map.put("id", mAuth.getCurrentUser().getUid());
                    map.put("screen", Global.myscreen);
                    map.put("lastmessage", encrypI);
                    map.put("lastsender", mAuth.getCurrentUser().getUid());
                    map.put("lastsenderava",Global.avaLocal);
                    map.put("messDate", currTime);
                    mData.child(friendId).child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendIpre(String.valueOf(downloadUrl));
                        }
                    });

                }
            }
        });
    }

    private void sendIpre(String link) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkI", encrypL);
        map.put("time", currTime);
        map.put("messId", imageA.get(iq));
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", true);
        map.put("seen", false);
        map.put("type", "image");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("from", mAuth.getCurrentUser().getUid());
        final String mssgid = imageA.get(iq);
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).child(mssgid)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages).child(mssgid)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //send friend data to owner
                        mAuth = FirebaseAuth.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("avatar",Global.currAva);
                        map.put("name",Global.currname);
                        map.put("nameL",Global.currname);
                        map.put("phone",Global.currphone);
                        map.put("screen", Global.currscreen);
                        map.put("lastmessage", encrypI);
                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                        map.put("lastsenderava",Global.avaLocal);
                        map.put("messDate", currTime);
                        map.put("id", friendId);
                        //  ------------
                        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypI, imageA.get(iq));
                                if (iq <= imageA.size() - 2) {
                                    //nothing
                                } else {
                                    iqb = true;

                                }

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iqb = true;
            }
        });
        if (iq <= imageA.size() - 2)
            iq++;

    }

    private void sendFpre(String link, String filename) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkF", encrypL);
        map.put("time", currTime);
        map.put("messId", fileA.get(fq));
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", true);
        map.put("filename", filename);
        map.put("seen", false);
        map.put("type", "file");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("from", mAuth.getCurrentUser().getUid());
        final String mssgid = fileA.get(fq);
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).child(mssgid)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages).child(mssgid)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //send friend data to owner
                        mAuth = FirebaseAuth.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("avatar",Global.currAva);
                        map.put("name",Global.currname);
                        map.put("nameL",Global.currname);
                        map.put("phone",Global.currphone);
                        map.put("screen", Global.currscreen);
                        map.put("lastmessage", encrypF);
                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                        map.put("lastsenderava",Global.avaLocal);
                        map.put("messDate", currTime);
                        map.put("id", friendId);
                        //  ------------
                        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendMessNotify(encrypF, fileA.get(fq));
                                if (fq <= fileA.size() - 2) {
                                    //nothing
                                } else {
                                    fqb = true;

                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                fqb = true;
            }
        });
        if (fq <= fileA.size() - 2)
            fq++;
    }

    private void sendVpre(String link, long time) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkV", encrypL);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", true);
        map.put("duration", getHumanTimeText(time));
        map.put("seen", false);
        map.put("type", "voice");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("from", mAuth.getCurrentUser().getUid());
        final String mssgid = messidL;
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).child(mssgid)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages).child(mssgid)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //send friend data to owner
                        mAuth = FirebaseAuth.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("avatar",Global.currAva);
                        map.put("name",Global.currname);
                        map.put("nameL",Global.currname);
                        map.put("phone",Global.currphone);
                        map.put("screen", Global.currscreen);
                        map.put("lastmessage", encrypV);
                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                        map.put("lastsenderava",Global.avaLocal);
                        map.put("messDate", currTime);
                        map.put("id", friendId);
                        //  ------------
                        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                sendMessNotify(encrypV, mssgid);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
            }
        });
    }

    private void sendVideopre(String link, long time, String thumbL) {
        encrypL = link;
        encrypL = encryption.encryptOrNull(encrypL);
        final Map<String, Object> map = new HashMap<>();
        map.put("linkVideo", encrypL);
        map.put("time", currTime);
        map.put("messId", messidL);
        map.put("react", "no");
        map.put("avatar", encryption.encryptOrNull(Global.avaLocal));
        map.put("chat", true);
        map.put("thumb", thumbL);
        map.put("duration", getHumanTimeText(time));
        map.put("seen", false);
        map.put("type", "video");
        map.put("deleted", false);
        map.put("statue", "✔");
        map.put("from", mAuth.getCurrentUser().getUid());
        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).child(Global.Messages).child(messidL)
                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mData.child(friendId).child(mAuth.getCurrentUser().getUid()).child(Global.Messages).child(messidL)
                        .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //send friend data to owner
                        mAuth = FirebaseAuth.getInstance();
                        Map<String, Object> map = new HashMap<>();
                        map.put("avatar",Global.currAva);
                        map.put("name",Global.currname);
                        map.put("nameL",Global.currname);
                        map.put("phone",Global.currphone);
                        map.put("screen", Global.currscreen);
                        map.put("lastmessage", encrypVideo);
                        map.put("lastsender", mAuth.getCurrentUser().getUid());
                        map.put("lastsenderava",Global.avaLocal);
                        map.put("messDate", currTime);
                        map.put("id", friendId);
                        //  ------------
                        mData.child(mAuth.getCurrentUser().getUid()).child(friendId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                sendMessNotify(encrypVideo, messidL);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private boolean startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioChannels(2);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        mRecorder.setAudioEncodingBitRate(48000);

        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            startAT();
            return true;
        } catch (Exception e) {
            Log.wtf("keyyyy",e.getMessage());
            return false;
        }
    }


    protected void stopRecording(boolean saveFile) {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            stopAT();
            if (!saveFile && mOutputFile != null) {
                mOutputFile.delete();

            }
        } catch (NullPointerException e) {
            stopAT();
        } catch (RuntimeException e) {
            mOutputFile.delete();
            stopAT();
        }

    }

    private File getOutputFile() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/" + getResources().getString(R.string.app_name) + "/Voice Notes/" + "rec_voice_" + String.valueOf(System.currentTimeMillis())
                + ".m4a");
    }

    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void editInf() {
        name.setText(Global.currname);
        if (String.valueOf(Global.currAva).equals("no")) {
            Picasso.get()
                    .load(R.drawable.profile)
                    .error(R.drawable.errorimg)
                    .into(ava);
        } else {
            Picasso.get()
                    .load(Global.currAva)
                    .error(R.drawable.errorimg)
                    .into(ava);
        }
        if (Global.check_int(this))
            state.setVisibility(View.VISIBLE);
        else
            state.setVisibility(View.GONE);

        if (Global.onstate) {
            if (typingR)
                state.setText(R.string.typing);
            else
                state.setText(getResources().getString(R.string.online));

            if (recordingR)
                state.setText(R.string.recording);
            else
                state.setText(getResources().getString(R.string.online));
        } else {
            state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));
            final ExecutorService es = Executors.newCachedThreadPool();
            ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
            ses.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    es.submit(new Runnable() {
                        @Override
                        public void run() {
                            state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));
                            Toast.makeText(Chat.this, GetTime.getTimeAgo(Global.currtime, Chat.this), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 0, 1, TimeUnit.MINUTES);
        }

    }

    public void typingit() {

        if (Global.onstate) {
            if (typingR)
                state.setText(R.string.typing);
            else if (recordingR)
                state.setText(R.string.recording);
            else
                state.setText(getResources().getString(R.string.online));

        } else {
            state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));
            final ExecutorService es = Executors.newCachedThreadPool();
            ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
            ses.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    es.submit(new Runnable() {
                        @Override
                        public void run() {
                            state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));
                            Toast.makeText(Chat.this, GetTime.getTimeAgo(Global.currtime, Chat.this), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 0, 1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void onResume() {
        pausebreak = false;
        //get realtime time
        h.postDelayed(runnable = new Runnable() {
            public void run() {
                if (!Global.onstate)
                    state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));
                h.postDelayed(runnable, TIMEUPDATE);
            }
        }, TIMEUPDATE);


        //resume
        super.onResume();
        if (!Global.onstate)
            state.setText(GetTime.getTimeAgo(Global.currtime, Chat.this));

        AppBack myApp = (AppBack) this.getApplication();
        if (myApp.wasInBackground) {
            //init data
            Map<String, Object> map = new HashMap<>();
            map.put(Global.Online, true);
            myData.child(mAuth.getCurrentUser().getUid()).updateChildren(map);
            Global.local_on = true;
            //lock screen
            ((AppBack) getApplication()).lockscreen(((AppBack) getApplication()).shared().getBoolean("lock", false));
        }
        myApp.stopActivityTransitionTimer();
        Global.currentpageid = friendId;
        //clear all notifications
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            if (notificationManager != null) {
                notificationManager.cancelAll();
                int count = 0;
                //store it again
                ((AppBack) getApplication()).editSharePrefs().putInt("numN" + mAuth.getCurrentUser().getUid(), count);
                ((AppBack) getApplication()).editSharePrefs().apply();
                ShortcutBadger.applyCount(this, count);
            }
        } catch (NullPointerException e) {
            //nothing
        }
        //read messages
        if (code == 0) {
            zeroCount();
            readM();
        }
        //check contact exist

        Dexter.withActivity(Chat.this)
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted())
                            contactExists(Global.currphone);

                        else
                            Toast.makeText(Chat.this, getString(R.string.acc_per), Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();
        Global.currentactivity = this;
    }

    @Override
    public void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
        ((AppBack) this.getApplication()).startActivityTransitionTimer();
        pausebreak = true;
        try {
            stopRecording(false);
        } catch (RuntimeException e) {
            mOutputFile.delete();
        }

        try {
            for(int i=0;i<Global.audiolist.size();i++)
                Global.audiolist.get(i).pause();
        }
        catch (NullPointerException e)
        {

        }

        setResult(RESULT_CANCELED);
        Global.currentpageid = "";
    }

    private void getSize() {
        if (message.getLineCount() == message.getMaxLines()) {
            message.setMaxLines((message.getLineCount() + 1));
        }

    }

    private void sendMessNotify(final String message, final String Mid) {


        DatabaseReference mTokenget = FirebaseDatabase.getInstance().getReference(Global.tokens);
        mTokenget.child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tokens tokens = dataSnapshot.getValue(Tokens.class);
                Map<String, String> map = new HashMap<>();
                map.put("title", tokens + "#" + mAuth.getCurrentUser().getUid() + "#" + Global.nameLocal + "#" + Global.avaLocal + "#" + Mid);
                map.put("message", message);
                Sender sender = new Sender(tokens.getTokens(), map);
                fcm.send(sender)
                        .enqueue(new Callback<FCMresp>() {
                            @Override
                            public void onResponse(Call<FCMresp> call, Response<FCMresp> response) {
                            }

                            @Override
                            public void onFailure(Call<FCMresp> call, Throwable t) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void goP(View view) {
        Intent intent = new Intent(this, Profile.class);
        intent.putExtra("idP", friendId);
        startActivity(intent);
    }

    public void contactExists(final String number) {
        final ArrayList<String> localcontact = new ArrayList<>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("(", "");
            phone = phone.replace(")", "");

            if (String.valueOf(phone.charAt(0)).equals("0") && String.valueOf(phone.charAt(1)).equals("0")) {
                StringBuilder sb = new StringBuilder(phone);
                sb.delete(0, 4);
                phone = sb.toString();
            }

            if (String.valueOf(phone.charAt(0)).equals("+")) {
                StringBuilder sb = new StringBuilder(phone);
                sb.delete(0, 3);
                phone = sb.toString();
            }
            if (!localcontact.contains(phone))
                localcontact.add(phone);
        }
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (number != null) {
                            for (int i = 0; i < localcontact.size(); i++) {
                                if (number.contains(localcontact.get(i))) {
                                    contactExistsLay(true);
                                    break;
                                } else {
                                    if (i == localcontact.size() - 1) {
                                        contactExistsLay(false);
                                        localcontact.clear();
                                    }

                                }
                            }
                        }
                    }
                }
        );

    }

    public void contactExistsLay(boolean exist) {
        boolean check = preferences.getBoolean("close_" + friendId + "_" + mAuth.getCurrentUser().getUid(), false);
        if (!exist && !check)
            existlay.setVisibility(View.VISIBLE);
        else
            existlay.setVisibility(View.GONE);

    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, Options.init().setRequestCode(100));
                } else {
                    Toast.makeText(Chat.this, getResources().getString(R.string.approve_upload), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {

        try {
            for(int i=0;i<Global.audiolist.size();i++)
                Global.audiolist.get(i).pause();
        }
        catch (NullPointerException e)
        {

        }


        Global.btnid.clear();
        Global.audiolist.clear();
        for (int i = 0; i < Global.messG.size(); i++) {
            //check all failed messages
            if (Global.messG.get(i).getStatue().equals(".."))
            {
                //make it false
                Global.messG.get(i).setStatue("X");
                ((AppBack) getApplication()).setchatsdb(friendId);

                //put to retry
                ((AppBack) getApplication()).getRetry(friendId);
                Global.messG.get(i).setStatue("..");
                Global.retryM.add(Global.messG.get(i));
                ((AppBack) getApplication()).setRetry(friendId);
            }
        }
       messagesAdapter.clear();
        messagesAdapter.addToEnd(MessageData.getMessages(), true);
        messagesAdapter.notifyDataSetChanged();
        super.onDestroy();
    }
}