package com.egrcc.search;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.egrcc.search.utils.GfsosoSearchUtil;
import com.egrcc.search.utils.VdiskSearchUtil;
import com.egrcc.search.utils.WangpanUtils;

public class MainActivity extends Activity implements OnQueryTextListener,OnCloseListener,OnClickListener {

    private SearchView searchView;
    private ListView listView;
    private ProgressBar mainProgressBar;
    private ImageView searchLogo;
    private DownloadThread downloadThread;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private LinearLayout mainLinearLayout;
    private LinearLayout searchLinearLayout;
    private EditText editText;
    private ImageButton searchButton;
    private LinearLayout focusView;

    private SimpleAdapter mSimpleAdapter;
    private Button loadmoreButton;
    private ProgressBar loadProgressBar;
    private ArrayList<HashMap<String,String>> list;
    private View moreView;
    private View searchTitle;
    private Handler dataHandler;
    private Handler moreDataHandler;
    
    class DownloadThread extends Thread {

        public Handler mHandler;
        private int count = 2;
        private ArrayList<String> resultUrl;
        private ArrayList<String> resultContent;
    	
        @Override
        public void run() {
            Looper.prepare();
            mHandler = new Handler(){
    			
                @Override
                public void handleMessage(Message msg){
                    if (msg.what == 0x123) {
                        count = 2;
                        String[] keywords = msg.getData().getString("KEYWORDS").split(" ");
                        String html = GfsosoSearchUtil.getHtml(keywords, 1, WangpanUtils.BAIDU_YUN);
                        resultUrl = GfsosoSearchUtil.getResultUrl(html);
                        resultContent = GfsosoSearchUtil.getResultContent(html);
                        System.out.println("GfsosoSearch");
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArrayList("RESULTURL", resultUrl);
                        resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                        Message msgData = new Message();
                        msgData.what = 0x1233;
                        msgData.setData(resultBundle);
                        dataHandler.sendMessage(msgData);
                    }
                    if (msg.what == 0x234) {
                        String[] keywords = msg.getData().getString("KEYWORDS").split(" ");
                        String html = GfsosoSearchUtil.getHtml(keywords, count, WangpanUtils.BAIDU_YUN);
                        resultUrl = GfsosoSearchUtil.getResultUrl(html);
                        resultContent = GfsosoSearchUtil.getResultContent(html);
                        System.out.println("GfsosoSearch");
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArrayList("RESULTURL", resultUrl);
                        resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                        Message msgData = new Message();
                        msgData.what = 0x2344;
                        msgData.setData(resultBundle);
                        moreDataHandler.sendMessage(msgData);
                        count++;
                    }
                    if (msg.what == 0x345) {
                        count = 2;
                        String[] keywords = msg.getData().getString("KEYWORDS").split(" ");
                        String html = VdiskSearchUtil.getHtml(keywords, 1);
                        resultUrl = VdiskSearchUtil.getResultUrl(html);
                        resultContent = VdiskSearchUtil.getResultContent(html);
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArrayList("RESULTURL", resultUrl);
                        resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                        Message msgData = new Message();
                        msgData.what = 0x3455;
                        msgData.setData(resultBundle);
                        dataHandler.sendMessage(msgData);
                    }
                    if (msg.what == 0x456) {
                        String[] keywords = msg.getData().getString("KEYWORDS").split(" ");
                        String html = VdiskSearchUtil.getHtml(keywords, count);
                        resultUrl = VdiskSearchUtil.getResultUrl(html);
                        resultContent = VdiskSearchUtil.getResultContent(html);
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArrayList("RESULTURL", resultUrl);
                        resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                        Message msgData = new Message();
                        msgData.what = 0x4566;
                        msgData.setData(resultBundle);
                        moreDataHandler.sendMessage(msgData);
                        count++;
                    }
                    if (msg.what == 0x567) {
                        count = 2;
                        String[] keywords = msg.getData().getString("KEYWORDS").split(" ");
                        String html = GfsosoSearchUtil.getHtml(keywords, 1, WangpanUtils.DBANK);
                        resultUrl = GfsosoSearchUtil.getResultUrl(html);
                        resultContent = GfsosoSearchUtil.getResultContent(html);
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArrayList("RESULTURL", resultUrl);
                        resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                        Message msgData = new Message();
                        msgData.what = 0x5677;
                        msgData.setData(resultBundle);
                        dataHandler.sendMessage(msgData);
                    }
                    if (msg.what == 0x678) {
                        String[] keywords = msg.getData().getString("KEYWORDS").split(" ");
                        String html = GfsosoSearchUtil.getHtml(keywords, count, WangpanUtils.DBANK);
                        resultUrl = GfsosoSearchUtil.getResultUrl(html);
                        resultContent = GfsosoSearchUtil.getResultContent(html);
                        Bundle resultBundle = new Bundle();
                        resultBundle.putStringArrayList("RESULTURL", resultUrl);
                        resultBundle.putStringArrayList("RESULTCONTENT", resultContent);
                        Message msgData = new Message();
                        msgData.what = 0x6788;
                        msgData.setData(resultBundle);
                        moreDataHandler.sendMessage(msgData);
                        count++;
                    }
                }
            };
            Looper.loop();
    		
    	}
    	
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadThread = new DownloadThread();
        downloadThread.start();
        searchLogo = (ImageView)findViewById(R.id.searchLogo);
        listView = (ListView)findViewById(R.id.listView);
        mainProgressBar = (ProgressBar)findViewById(R.id.mainProgressBar);
        radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
        mainLinearLayout = (LinearLayout)findViewById(R.id.mainLinearLayout);
        searchLinearLayout = (LinearLayout)findViewById(R.id.searchLinearLayout);
        focusView = (LinearLayout)findViewById(R.id.focusView);
        editText = (EditText)findViewById(R.id.editText);
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        searchTitle = getLayoutInflater().inflate(R.layout.listview_header, null);
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);
        loadmoreButton = (Button) moreView.findViewById(R.id.loadmoreButton);
        loadProgressBar = (ProgressBar) moreView.findViewById(R.id.loadProgressBar);
        
        searchButton.setOnClickListener(MainActivity.this);
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
    	}
    	catch (Exception e) {
    	  
    	}
//        if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView)menu.findItem(R.id.searchView).getActionView();
        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.search); 
        searchView.setOnQueryTextListener(MainActivity.this);
        searchView.setSubmitButtonEnabled(true);
		
        searchView.setOnCloseListener(MainActivity.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return true;
    }
	
    private void loadMoreData(ArrayList<String> moreResultUrl, ArrayList<String> moreResultContent) {
        for (int i = 0; i < moreResultUrl.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", moreResultContent.get(i));
            map.put("ItemText", moreResultUrl.get(i));
            list.add(map);
        }
    }

    @Override
    public boolean onClose() {
        searchLogo.setVisibility(View.VISIBLE);
        searchLinearLayout.setVisibility(View.VISIBLE);
        mainLinearLayout.setBackgroundColor(getResources().getColor(R.color.background1));
        mainProgressBar.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        return false;
    }

    @Override
    public void onClick(View v) {
        search(editText.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainLinearLayout.setBackgroundColor(getResources().getColor(R.color.background1));
    }
	
    private void search(String keywordString) {
        Message msg = new Message();
        if (radioButton1.isChecked() == true) {
            msg.what = 0x123;
        } else if (radioButton2.isChecked() == true) {
            msg.what = 0x345;
        } else if (radioButton3.isChecked() == true) {
            msg.what = 0x567;
        }
        final Bundle bundle = new Bundle();
        bundle.putString("KEYWORDS", keywordString);
        msg.setData(bundle);
        downloadThread.mHandler.sendMessage(msg);
        
        searchLogo.setVisibility(View.GONE);
        searchLinearLayout.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        mainLinearLayout.setBackgroundColor(getResources().getColor(R.color.background2));
        mainProgressBar.setVisibility(View.VISIBLE);
    	InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    	View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        
        
        dataHandler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                if (msg.what == 0x1233 || msg.what == 0x3455 || msg.what == 0x5677) {
        			
                    list = new ArrayList<HashMap<String,String>>();
                    ArrayList<String> resultUrl = new ArrayList<String>();
                    ArrayList<String> resultContent = new ArrayList<String>();
                    resultUrl = msg.getData().getStringArrayList("RESULTURL");
                    resultContent = msg.getData().getStringArrayList("RESULTCONTENT");
                    if (resultUrl.size() == 0) {
                        Toast.makeText(MainActivity.this, "没有找到资源！", Toast.LENGTH_LONG).show();
                        mainProgressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < resultUrl.size(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemTitle", resultContent.get(i));
                            map.put("ItemText", resultUrl.get(i));
                            list.add(map);
                        }
            	        mSimpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.listview_item,
            	                new String[] { "ItemTitle", "ItemText" },
            	                new int[] { R.id.tv_title, R.id.tv_content });
            	        mainProgressBar.setVisibility(View.GONE);
            	        listView.setVisibility(View.VISIBLE);
            	        focusView.setFocusable(true);
            	        focusView.setFocusableInTouchMode(true);
            	        focusView.requestFocus();
            	        
            	        if (listView.getFooterViewsCount() == 0) {
            	            listView.addFooterView(moreView);
            	        }
            	        if (listView.getHeaderViewsCount() > 0) {
            	            listView.removeHeaderView(searchTitle);
            	        }
            	        if (listView.getHeaderViewsCount() == 0 && radioButton2.isChecked() == false) {
            	            listView.addHeaderView(searchTitle, null, false);
            	        }
            	        listView.setAdapter(mSimpleAdapter);
            	        listView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                                focusView.setFocusable(true);
                                focusView.setFocusableInTouchMode(true);
                                focusView.requestFocus();
                                if (radioButton2.isChecked() == false) {
                                    System.out.println(list.get(position - 1).get("ItemTitle"));
                                    System.out.println(list.get(position - 1).get("ItemText"));
                                } else {
                                    System.out.println(list.get(position).get("ItemTitle"));
                                    System.out.println(list.get(position).get("ItemText"));
                                }
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri url;
                                if (radioButton2.isChecked() == false) {
                                    url = Uri.parse(list.get(position - 1).get("ItemText"));
                                } else {
                                    url = Uri.parse(list.get(position).get("ItemText"));
                                }
                                intent.setData(url);
                                startActivity(intent);

                            }

                        });
            	        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(
                                AdapterView<?> parent, View view,
                                int position, long id) {

                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                if (radioButton2.isChecked() == false) {
                                    clipboard.setPrimaryClip(ClipData.newPlainText("data", list.get(position - 1).get("ItemText")));
                                } else {
                                    clipboard.setPrimaryClip(ClipData.newPlainText("data", list.get(position).get("ItemText")));
                                }
                                Toast.makeText(MainActivity.this, "链接已复制！", Toast.LENGTH_SHORT).show();
                                return true;
                            }

                        });
            	        
            	        
            	        loadmoreButton.setOnClickListener(new OnClickListener() {

            	            @Override
            	            public void onClick(View v) {
            	            	loadmoreButton.setVisibility(View.GONE);
            	                loadProgressBar.setVisibility(View.VISIBLE);
            	                Message moreDataMsg = new Message();
            	                if (radioButton1.isChecked() == true) {
            	                    moreDataMsg.what = 0x234;
            	                } else if (radioButton2.isChecked() == true) {
                                    moreDataMsg.what = 0x456;
//                                  System.out.println("button2");
            	                } else if (radioButton3.isChecked() == true) {
            	                    moreDataMsg.what = 0x678;
            	                }
            	                moreDataMsg.setData(bundle);
            	                downloadThread.mHandler.sendMessage(moreDataMsg);
            	                moreDataHandler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg){
                                        if (msg.what == 0x2344 || msg.what == 0x4566 || msg.what == 0x6788) {
                                            ArrayList<String> moreResultUrl = new ArrayList<String>();
                                            ArrayList<String> moreResultContent = new ArrayList<String>();
                                            moreResultUrl = msg.getData().getStringArrayList("RESULTURL");
                                            moreResultContent = msg.getData().getStringArrayList("RESULTCONTENT");
                                            if (moreResultUrl.size() == 0) {
                                                Toast.makeText(MainActivity.this, "没有更多了！", Toast.LENGTH_LONG).show();
                                                loadmoreButton.setVisibility(View.GONE);
                                                loadProgressBar.setVisibility(View.GONE);
                                            } else {
                                                loadMoreData(moreResultUrl, moreResultContent);
                                                loadmoreButton.setVisibility(View.VISIBLE);
                                                loadProgressBar.setVisibility(View.GONE);
                                                mSimpleAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
            	                };
            	            }
            	        });
                    }
        	        
                }
            }
        };
    }
		
}
