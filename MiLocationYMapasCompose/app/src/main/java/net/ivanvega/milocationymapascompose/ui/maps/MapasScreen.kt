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
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
fun MiMapaWithCameraControl(){
    // Definir la posición inicial de la cámara
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    // Refactoriza el estado de la cámara para poder actualizarlo
    var cameraPosition by remember { mutableStateOf(cameraPositionState.position) }

    // Función para mover la cámara a una nueva posición
    fun moveCameraToPosition(newPosition: LatLng) {
        cameraPosition = CameraPosition.Builder()
            .target(newPosition)
            .zoom(10f) // zoom nivel 10 como ejemplo
            .build()
    }

    // Función para mover la cámara a una nueva posición con un zoom específico
    fun moveCameraToPositionWithZoom(newPosition: LatLng, zoomLevel: Float) {
        cameraPosition = CameraPosition.Builder()
            .target(newPosition)
            .zoom(zoomLevel)
            .build()
    }

    // Función para mover la cámara con un desplazamiento específico
    fun moveCameraWithOffset(offset: Float) {
        val currentZoom = cameraPosition.zoom
        cameraPosition = CameraPosition.Builder()
            .target(cameraPosition.target)
            .zoom(currentZoom + offset)
            .build()
    }

    LaunchedEffect(cameraPosition) {
        // Esta lambda se ejecutará cada vez que cambie la posición de la cámara
        cameraPositionState.position = cameraPosition
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Muestra el marcador en la posición actual de la cámara
        Marker(
            state = MarkerState(position = cameraPosition.target),
            title = "Current Position",
            snippet = "Marker at current position"
        )
    }

    // Agrega botones u otros elementos de interfaz de usuario para controlar la cámara
    // Ejemplo de botones para mover la cámara
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
    }
}