package pokemon.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Galeria extends Activity {
	
	ImageView foto;
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galery);
        
        foto = (ImageView) findViewById(R.id._imagePokemon);
        
        TextView text = (TextView) findViewById(R.id._description);
        text.setText("Pepito grillo y los malotes de Pinocho");
        
        
        Button next = (Button) findViewById(R.id.button1);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), PokemonMapsActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
        
        Gallery gallery = (Gallery) findViewById(R.id._galeria);
        gallery.setAdapter(new ImageAdapter(this));

        gallery.setOnItemClickListener(new OnItemClickListener() {      	
        	
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                foto.setImageResource(ImageAdapter.mImageIds[position]);
                
                PokemonMapsActivity._indexPokemon = position;
            }
        });
    }
    
    public static class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        
        private static Integer[] mImageIds = {
            R.drawable.arceus,
            R.drawable.hippowdon,
            R.drawable.pichu,
            R.drawable.claydol,
            R.drawable.swellow,
            R.drawable.zekrom
        };

        public ImageAdapter(Context c) {
            mContext = c;
            TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = attr.getResourceId(R.styleable.HelloGallery_android_galleryItemBackground, 0);
            attr.recycle();
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);

            imageView.setImageResource(mImageIds[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(mGalleryItemBackground);
            
            return imageView;
        }
    }
}
