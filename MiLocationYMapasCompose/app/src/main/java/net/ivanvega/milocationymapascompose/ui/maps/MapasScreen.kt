package net.ivanvega.milocationymapascompose.ui.maps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import androidx.compose.runtime.*
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.ktx.awaitMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import android.location.Location
import com.google.maps.android.ktx.*

@Composable
fun MiMapa(){
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )
    }
}
@Composable
fun MapWithCameraAndDrawing() {
    var drawnPoints by remember { mutableStateOf(emptyList<LatLng>()) }
    var drawnPolyline by remember { mutableStateOf<List<LatLng>?>(null) }
    var drawnPolygon by remember { mutableStateOf<List<LatLng>?>(null) }
    var drawnCircleCenter by remember { mutableStateOf<LatLng?>(null) }
    var drawnCircleRadius by remember { mutableStateOf<Float?>(null) }
    var isDrawingCircle by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        CameraPosition.Builder()
            .target(LatLng(1.35, 103.87))
            .zoom(10f)
            .build()
    }

    fun startDrawingCircleMode() {
        isDrawingCircle = true
    }

    fun calculateRadius(points: List<LatLng>): Float {
        val firstPoint = points.firstOrNull()
        val lastPoint = points.lastOrNull()

        return if (firstPoint != null && lastPoint != null) {
            val results = FloatArray(1)
            Location.distanceBetween(
                firstPoint.latitude,
                firstPoint.longitude,
                lastPoint.latitude,
                lastPoint.longitude,
                results
            )
            results[0]
        } else {
            0f
        }
    }

    fun moveCameraToPosition(newPosition: LatLng) {
        cameraPositionState.position = CameraPosition.Builder()
            .target(newPosition)
            .zoom(10f)
            .build()
    }

    fun moveCameraToPositionWithZoom(newPosition: LatLng, zoomLevel: Float) {
        cameraPositionState.position = CameraPosition.Builder()
            .target(newPosition)
            .zoom(zoomLevel)
            .build()
    }

    fun moveCameraWithOffset(offset: Float) {
        val currentZoom = cameraPositionState.position.zoom
        cameraPositionState.position = CameraPosition.Builder()
            .target(cameraPositionState.position.target)
            .zoom(currentZoom + offset)
            .build()
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            drawnPoints = drawnPoints + latLng
            drawnPolyline = drawnPoints.takeLast(2) // Update the polyline
            if (drawnPoints.size >= 3) {
                drawnPolygon = drawnPoints // Update the polygon
            }
            if (isDrawingCircle) {
                drawnCircleCenter = latLng // Update the circle center
                drawnCircleRadius = calculateRadius(drawnPoints) // Update the circle radius
            }
        }
    ) {
        drawnPolyline?.let { polyline ->
            Polyline(points = polyline, color = Color.Blue, width = 5F)
        }
        drawnPolygon?.let { polygon ->
            Polygon(points = polygon, fillColor = Color.Red.copy(alpha = 0.5f))
        }
        drawnCircleCenter?.let { center ->
            drawnCircleRadius?.let { radius ->
                Circle(center = center, radius = radius.toDouble(), fillColor = Color.Green.copy(alpha = 0.5f))
            }
        }
        drawnPoints.forEach { point ->
            Marker(
                state = MarkerState(position = point)
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { moveCameraToPosition(LatLng(1.35, 103.87)) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Move to Singapore")
        }
        Button(
            onClick = { moveCameraToPositionWithZoom(LatLng(1.0, 103.0), 15f) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Move to Position with Zoom")
        }
        Button(
            onClick = { moveCameraWithOffset(1f) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Zoom In")
        }
        Button(
            onClick = { moveCameraWithOffset(-1f) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Zoom Out")
        }
        Button(
            onClick = { startDrawingCircleMode() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Draw Circle")
        }
        Button(
            onClick = { isDrawingCircle = false },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Exit Circle Mode")
        }
    }
}