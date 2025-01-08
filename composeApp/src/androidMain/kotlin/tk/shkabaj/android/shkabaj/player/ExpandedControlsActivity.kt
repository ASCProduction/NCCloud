package tk.shkabaj.android.shkabaj.player

import android.view.Menu
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity
import tk.shkabaj.android.shkabaj.R

class ExpandedControlsActivity : ExpandedControllerActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_cast_expanded_controller, menu)
        if (menu != null) {
            CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item)
        }
        return true
    }
}