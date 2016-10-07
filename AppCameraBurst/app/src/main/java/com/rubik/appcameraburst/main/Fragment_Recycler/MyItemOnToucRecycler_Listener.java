package com.rubik.appcameraburst.main.Fragment_Recycler;

import android.view.View;

/**
 * Created by Rubik on 7/10/16.
 */

public interface MyItemOnToucRecycler_Listener {

    /**
     * Interface for Recycler View Click listener
     **/

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
