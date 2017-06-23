 package com.zeyou.zeyousdklib.widget;

 import android.content.Context;
 import android.util.AttributeSet;
 import android.widget.RelativeLayout;

 public class ContainerLayout
   extends RelativeLayout
 {
   ReFixListener reFix;
   int width;
   int height;

   public ReFixListener getReFix()
   {
     return this.reFix;
   }

   public void setReFix(ReFixListener reFix)
   {
     this.reFix = reFix;
   }

   public ContainerLayout(Context context)
   {
     super(context);
   }

   public ContainerLayout(Context context, AttributeSet attrs)
   {
     super(context, attrs);
   }

   public ContainerLayout(Context context, AttributeSet attrs, int defStyleAttr)
   {
     super(context, attrs, defStyleAttr);
   }

   protected void onLayout(boolean changed, int l, int t, int r, int b)
   {
     super.onLayout(changed, l, t, r, b);
     if ((r - l != this.width) && (b - t != this.height) && (this.reFix != null))
     {
       this.width = (r - l);
       this.height = (b - t);
       this.reFix.onReFix();
     }
   }

   public static abstract interface ReFixListener
   {
     public abstract void onReFix();
   }
 }



