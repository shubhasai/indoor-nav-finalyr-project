package com.shubhasai.indoornavigation

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.shubhasai.indoornavigation.databinding.ActivityMainBinding
import com.shubhasai.indoornavigation.models.Location
import com.shubhasai.indoornavigation.models.Path
import com.shubhasai.indoornavigation.views.IndoorMapView
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Predefined paths (replace with your actual data)
//        val mainCorridorPath = Path(
//            "Main Corridor", 1, listOf(
//                Location(1, 100.0f, 30.5f),  // Top left corner
//                Location(1, 700.0f, 30.5f),  // Top right corner
//                Location(1, 700.0f, 100.0f), // Bottom right corner
//                Location(1, 100.0f, 100.0f)  // Bottom left corner (completes rectangle)
//            )
//        )
//char*text ="x=300.0, y=200.0, floor=1";

//
//// Example locations (replace with your actual locations)
//        val restroomLocation = Location(
//            1, 400.5f, 200.7f, "Restroom", R.drawable.ic_restroom
//        )
//        val starbucksLocation = Location(
//            1, 700.2f, 500.1f, "Starbucks", R.drawable.ic_store
//        )
//        val stores = listOf(Location(1, 150.0f, 40.0f, "Store 1", R.drawable.ic_store),
//            Location(1, 250.0f, 40.0f, "Store 2", R.drawable.ic_store),
//            Location(1, 350.0f, 40.0f, "Store 3", R.drawable.ic_store),
//            Location(1, 450.0f, 40.0f, "Store 4", R.drawable.ic_store),
//            Location(1, 550.0f, 40.0f, "Store 5", R.drawable.ic_store),
//            Location(1, 650.0f, 40.0f, "Store 6", R.drawable.ic_store),
//            Location(
//                1, 200.0f, 50.0f, "Restroom 1", R.drawable.ic_restroom
//            ),
//            Location(
//                1, 600.0f, 50.0f, "Restroom 2", R.drawable.ic_restroom
//            ),
//
//            Location(1, 150.0f, 0.0f, "Store 7", R.drawable.ic_store),
//            Location(1, 250.0f, 80.0f, "Store 8", R.drawable.ic_store),
//            Location(1, 350.0f, 80.0f, "Store 9", R.drawable.ic_store),
//            Location(1, 450.0f, 80.0f, "Store 10", R.drawable.ic_store),
//            Location(1, 550.0f, 80.0f, "Store 11", R.drawable.ic_store),
//            Location(1, 650.0f, 80.0f, "Store 12", R.drawable.ic_store),
//
//            Location(1, 100.0f, 50.0f, "Store 13", R.drawable.ic_store), // Left side, near restroom 1
//            Location(1, 700.0f, 50.0f, "Store 14", R.drawable.ic_store), // Right side, near restroom 2
//
//            Location(1, 120.0f, 60.0f, "Store 15", R.drawable.ic_store),
//            Location(1, 680.0f, 60.0f, "Store 16", R.drawable.ic_store),
//
//            Location(1, 130.0f, 70.0f, "Store 17", R.drawable.ic_store),
//            Location(1, 670.0f, 70.0f, "Store 18", R.drawable.ic_store),
//
//            Location(1, 100.0f, 90.0f, "Store 19", R.drawable.ic_store),)
        val mainCorridorPath = Path(
            "Main Corridor", 1, mutableListOf(
                Location(1, 250.0f, 100.0f),  // Top left corner
                Location(1, 450.0f, 100.0f),  // Top right corner
                Location(1, 450.0f, 900.0f), // Bottom right corner
                Location(1, 250.0f, 900.0f) ,
                Location(1, 250.0f, 100.0f),// Top left corner (completes rectangle)
            )
        )
        val shortcutPath = Path(
            "Shortcut", 1, mutableListOf(
                Location(1, 250.5f, 500.8f),
                Location(1, 450.5f, 300.2f)
            )
        )

        val stores = listOf(
            // Left side of corridor
            Location(1, 150.0f, 200.0f, "Store 1", R.drawable.ic_store),
            Location(1, 150.0f, 300.0f, "Store 2", R.drawable.ic_store),
            Location(1, 150.0f, 400.0f, "Store 3", R.drawable.ic_store),
            Location(1, 150.0f, 500.0f, "Store 4", R.drawable.ic_store),
            Location(1, 150.0f, 600.0f, "Store 5", R.drawable.ic_store),
            Location(1, 150.0f, 700.0f, "Store 6", R.drawable.ic_store),

            // Right side of corridor
            Location(1, 550.0f, 200.0f, "Store 7", R.drawable.ic_store),
            Location(1, 550.0f, 300.0f, "Store 8", R.drawable.ic_store),
            Location(1, 550.0f, 400.0f, "Store 9", R.drawable.ic_store),
            Location(1, 550.0f, 500.0f, "Store 10", R.drawable.ic_store),
            Location(1, 550.0f, 600.0f, "Store 11", R.drawable.ic_store),
            Location(1, 550.0f, 700.0f, "Store 12", R.drawable.ic_store),

            // Restrooms can be placed anywhere outside the main corridor (example)
            Location(1, 100.0f, 800.0f, "Restroom 1", R.drawable.ic_restroom),
            Location(1, 600.0f, 100.0f, "Restroom 2", R.drawable.ic_restroom),
        )

        val customMapView = IndoorMapView(this)

// Set current location (replace with actual values)
        customMapView.setLocation(1, 250.8f, 650.1f)

// Set stored locations
        customMapView.setStoredLocations(stores)
        var paths = listOf(mainCorridorPath,shortcutPath)

        val spath = findPath(stores,"Store 4","Store 8", paths)

        var pathss = listOf(mainCorridorPath,shortcutPath)
// Select a path to display (optional)
        customMapView.setSelectedPaths(pathss)
        binding.llContainer.addView(customMapView)
        Log.d("Calculated Path","$spath")
        setContentView(binding.root)
        binding.btnRead.setOnClickListener {
            readFromArduino()
        }
    }
    fun findPath(stores: List<Location>, startName: String, endName: String, availablePaths: List<Path>): Path? {
        val startLocation = stores.find { it.name == startName }
        val endLocation = stores.find { it.name == endName }

        if (startLocation == null || endLocation == null) {
            return null  // Handle cases where stores are not found
        }

        // Find nearest points on available paths for start and end locations
        var startNearest: Location? = null
        var endNearest: Location? = null
        var minStartDistance = Float.MAX_VALUE
        var minEndDistance = Float.MAX_VALUE

        for (path in availablePaths) {
            val tempStartNearest = findNearestPointOnPath(path, startLocation!!)
            val tempEndNearest = findNearestPointOnPath(path, endLocation!!)
            val startDistance = calculateDistance(startLocation.x, startLocation.y, tempStartNearest.x, tempStartNearest.y)
            val endDistance = calculateDistance(endLocation.x, endLocation.y, tempEndNearest.x, tempEndNearest.y)

            if (startDistance < minStartDistance) {
                minStartDistance = startDistance
                startNearest = tempStartNearest
            }
            if (endDistance < minEndDistance) {
                minEndDistance = endDistance
                endNearest = tempEndNearest
            }
        }

        // Check if no suitable nearest points were found on any path
        if (startNearest == null || endNearest == null) {
            return null  // Handle cases where stores are too far from all paths
        }

        val path = Path("Combined Path", 1, mutableListOf())

        // Handle cases based on store proximity to the nearest points found
        val startPath = availablePaths.find { it.pathPoints.contains(startNearest!!) }
        val endPath = availablePaths.find { it.pathPoints.contains(endNearest!!) }

        if (startPath != null) {
            // Add path segment from starting store location to nearest point on path (straight line)
            path.pathPoints.add(startLocation)
            path.pathPoints.add(startNearest!!)
        } else {
            // Starting store is far from all paths, consider alternative approach (e.g., user walks directly to a path entrance)
            // You might need to implement additional logic here
            return null  // Or provide alternative guidance
        }

        if (endPath != null) {
            // Add segment of path between nearest points (considering path direction)
            val startIndex = endPath.pathPoints.indexOf(startNearest!!)
            val endIndex = endPath.pathPoints.indexOf(endNearest!!)
            if (startIndex != -1 && endIndex != -1) {
                if (startIndex < endIndex) {
                    path.pathPoints.addAll(endPath.pathPoints.subList(startIndex, endIndex + 1))
                } else {
                    // Handle case where startNearest is after endNearest on the path (loop around)
                    path.pathPoints.addAll(endPath.pathPoints.subList(startIndex, endPath.pathPoints.size))
                    path.pathPoints.addAll(endPath.pathPoints.subList(0, endIndex + 1))
                }
            } else {
                // Handle cases where nearest points might not be on the same path segment (potentially indicating complex path layouts)
                // You might need additional logic to handle these scenarios
                return null  // Or provide alternative guidance
            }
        } else {
            // Ending store is far from all paths, consider alternative approach (e.g., user walks directly to a path entrance)
            // You might need to implement additional logic here
            return null  // Or provide alternative guidance
        }

        // Add path segment from nearest point on path to end location (straight line)
        path.pathPoints.add(endNearest!!)
        path.pathPoints.add(endLocation)

        return path
    }
    fun findNearestPointOnPath(path: Path, location: Location): Location {
        var nearestPoint: Location = path.pathPoints[0]  // Initialize with first point
        var minDistance = Float.MAX_VALUE  // Initialize with maximum distance

        for (pointInPath in path.pathPoints) {
            val distance = calculateDistance(location.x, location.y, pointInPath.x, pointInPath.y)
            if (distance < minDistance) {
                minDistance = distance
                nearestPoint = pointInPath
            }
        }
        return nearestPoint
    }

    // Helper function to calculate distance between two points (assuming x and y coordinates)
    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return Math.sqrt(((dx * dx) + (dy * dy)).toDouble()).toFloat()  // Euclidean distance
    }

    private fun readFromArduino() {
        val usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            Toast.makeText(this, "No USB device found", Toast.LENGTH_SHORT).show()
            return
        }

        val driver = availableDrivers.first()
        val connection = usbManager.openDevice(driver.device) ?: return

        val port = driver.ports[0] // Most devices have just one port (port 0)
        try {
            port.open(connection)
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)

            val buffer = ByteArray(256)
            var len = port.read(buffer, 1000)
            while (len > 0) {
                val data = String(buffer, 0, len)
                Log.d("Serial", "Read data: $data")

                // Extracting x, y, and floor values
                val x = getValueFromData(data, "x")
                val y = getValueFromData(data, "y")
                val floor = getValueFromData(data, "floor")

                // Logging and displaying extracted values
                Log.d("Serial", "x=$x, y=$y, floor=$floor")
                binding.tvSerialData.text = "${binding.tvSerialData.text}\nx=$x, y=$y, floor=$floor"
                len = port.read(buffer, 1000) // Keep reading data
            }
        } catch (e: IOException) {
            Log.e("Serial", "Error reading: ${e.message}")
        } finally {
            port.close()
            connection.close()
        }
    }
    // Function to extract value corresponding to a key from the data string
    private fun getValueFromData(data: String, key: String): String? {
        val pattern = "$key=([\\d.]+)".toRegex()
        val matchResult = pattern.find(data)
        return matchResult?.groupValues?.get(1)
    }

}