package com.topnews.helper;
import org.json.JSONObject;
import com.topnews.bean.Response;

public class ApiService extends HTTPAsyncTask{

	private String loading = "加载中...";
	private String failed  = "加载失败.";
	private OnServiceListener serviceListener = null;
	
	private JSONObject jsonResult;
	
	public ApiService()
	{
	}
	
	public ApiService(String apiUrl,String method)
	{
		this.setMethod(method);
		this.setUrl(apiUrl);
	}

	public ApiService(String apiUrl)
	{
		this.setMethod(Constants.GET);
		this.setUrl(apiUrl);
	}
	
	public void execute(OnServiceListener listener)
	{
		this.setServiceListener(listener);
		super.execute();
	}

	@Override
	protected void onPreExecute() {
		if(serviceListener!=null)
			serviceListener.OnPreExecute();

//		mDialog = new ProgressDialog(this.context);
//		mDialog.setMessage(loading);
//		mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//			
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				cancel(true);
//			}
//		});
//		mDialog.show();
	}

	@Override
	protected void onPostExecute(Response response) {
		if(response != null){
			JSONObject json = response.getContent();
			if(response.getStatusCode() == Constants.STATUS_SUCCESS){
				this.jsonResult = json;
				if(serviceListener!=null)
					serviceListener.onReceivedData(this.jsonResult);
			}else{
				if(serviceListener!=null)
					serviceListener.OnFailed();
			}
		}else{
			if(serviceListener!=null)
				serviceListener.OnFailed();
		}
	}

	public OnServiceListener getServiceListener() {
		return serviceListener;
	}

	private void setServiceListener(OnServiceListener serviceListener) {
		this.serviceListener = serviceListener;
	}

	public interface OnServiceListener {
	    public abstract void onReceivedData(JSONObject json);
	    public abstract void OnPreExecute();
	    public abstract void OnFailed();
	}
}
