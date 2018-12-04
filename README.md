# RecyclerViewAdapter
Android RecyclerView 通用适配器，支持多布局、单布局

## 依赖
这个还在摸索中，望大神指教，尽快弄成以下形式：

```
dependencies {
    implementation '***:x.x.x'
}
```

## 单布局使用：
[sample - TagAdapter](./sample/src/main/java/com/csp/sample/adapter/adapter/TagAdapter.java)
``` java
public class TagAdapter extends SingleAdapter<String> {

    /**
     * 添加布局 ID
     */
    public TagAdapter(Context context) {
        super(context, R.layout.item_tag);
    }

    /**
     * 布局和数据绑定
     */
    @Override
    protected void onBind(ViewHolder holder, String datum, final int position) {
        holder.setText(R.id.txt_tag, datum);
    }
}
```

## 多布局使用：
[sample - MixAdapter](./sample/src/main/java/com/csp/sample/adapter/adapter/MixAdapter.java)

[sample - TitleViewFill](./sample/src/main/java/com/csp/sample/adapter/adapter/TitleViewFill.java)

``` java
public class MixAdapter extends MultipleAdapter<TopDto> {
    private final int TOP_LAYOUT = 0;
    private final int TITLE_LAYOUT = 1;

    public MixAdapter(Context context) {
        super(context);
    }

    /**
     * 添加布局（布局数据类型可以不同）
     */
    @Override
    protected void addMultiViewFills() {
        addViewFill(TOP_LAYOUT, new TopViewFill());
        addViewFill(TITLE_LAYOUT, new TitleViewFill());
    }

    @Override
    public int getItemCount() {
        return mNewData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewType.get(position);
    }

    /**
     * 只负责将数据分配给对应的布局
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        IViewFill viewFill = getViewFill(viewType);
        if (viewType == TOP_LAYOUT) {
            ((TopViewFill) viewFill).onBind(holder, (TopDto) mNewData.get(position), position);
            return;
        }

        if (viewType == TITLE_LAYOUT) {
            ((TitleViewFill) viewFill).onBind(holder, (String) mNewData.get(position), position);
        }
    }
}
```
``` java
class TitleViewFill implements MultipleAdapter.IViewFill<String> {

    /**
     * 添加布局 ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.item_title;
    }

    /**
     * 布局和数据绑定
     */
    @Override
    public void onBind(final ViewHolder holder, String title, final int position) {
        holder.setText(R.id.txt_title, title);
    }
}
```

## 头尾布局
装饰者模式，任意 RecyclerView.Adapter 均可，实现原理为多布局
``` java
MixAdapter mixAdapter = new MixAdapter(this);
HeadFootAdapter adapter = new HeadFootAdapter(mixAdapter);
rcvMultiple.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
rcvMultiple.setAdapter(adapter);

View view = adapter.addHeaderView(R.layout.item_tag, rcvMultiple);
((TextView) view.findViewById(R.id.txt_tag)).setText("头布局");

view = adapter.addFootView(R.layout.item_tag, rcvMultiple);
((TextView) view.findViewById(R.id.txt_tag)).setText("尾布局");
```

## Sample
![sample.gif](./img/sample.gif)

## 感谢
- [张鸿洋 - baseAdapter](https://github.com/hongyangAndroid/baseAdapter)：主要参考对象，尤其是 ViewHolder 类
