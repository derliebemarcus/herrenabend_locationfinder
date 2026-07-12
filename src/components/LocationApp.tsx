import React, { useState, useEffect } from 'react';
import { MapPin, Share2, RefreshCw, Navigation, AlertCircle } from 'lucide-react';

interface LocationData {
  place_id?: string;
  name: string;
  vicinity: string;
  rating?: number;
  url?: string;
}

export default function LocationApp() {
  const [location, setLocation] = useState<LocationData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Parse placeId from URL on load
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const placeId = params.get('placeId');
    if (placeId && window.google) {
      fetchPlaceDetails(placeId);
    }
  }, []);

  const fetchPlaceDetails = (placeId: string) => {
    if (!window.google) {
      setError('Google Maps API nicht geladen.');
      return;
    }

    setLoading(true);
    setError(null);

    const map = new google.maps.Map(document.createElement('div'));
    const service = new google.maps.places.PlacesService(map);

    service.getDetails(
      {
        placeId,
        fields: ['name', 'vicinity', 'rating', 'url', 'place_id'],
      },
      (place, status) => {
        setLoading(false);
        if (status === google.maps.places.PlacesServiceStatus.OK && place) {
          setLocation({
            place_id: place.place_id,
            name: place.name || 'Unbekannt',
            vicinity: place.vicinity || '',
            rating: place.rating,
            url: place.url,
          });
        } else {
          setError('Location konnte nicht geladen werden.');
        }
      }
    );
  };

  const findNewLocation = () => {
    if (!window.google) {
      setError('Google Maps API nicht geladen.');
      return;
    }

    setLoading(true);
    setError(null);

    // Get user coordinates or fallback to Berlin center
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => searchPlaces(position.coords.latitude, position.coords.longitude),
        () => searchPlaces(52.52, 13.405) // Fallback Berlin
      );
    } else {
      searchPlaces(52.52, 13.405);
    }
  };

  const searchPlaces = (lat: number, lng: number) => {
    const map = new google.maps.Map(document.createElement('div'));
    const service = new google.maps.places.PlacesService(map);

    const request = {
      location: new google.maps.LatLng(lat, lng),
      radius: 2000,
      type: 'bar', // 'bar' covers most, we can also use keyword 'cafe'
    };

    service.nearbySearch(request, (results, status) => {
      setLoading(false);
      if (status === google.maps.places.PlacesServiceStatus.OK && results && results.length > 0) {
        
        // Filter out unwanted types as in the old java code
        const discardTypes = ['night_club', 'lodging', 'art_gallery', 'store'];
        const validPlaces = results.filter(p => {
          if (!p.types) return true;
          return !p.types.some(t => discardTypes.includes(t));
        });

        if (validPlaces.length === 0) {
          setError('Keine passenden Locations gefunden.');
          return;
        }

        // Pick a random place
        const randomPlace = validPlaces[Math.floor(Math.random() * validPlaces.length)];
        
        const locData = {
          place_id: randomPlace.place_id,
          name: randomPlace.name || 'Unbekannt',
          vicinity: randomPlace.vicinity || '',
          rating: randomPlace.rating,
        };
        
        setLocation(locData);

        // Update URL for deeplinking
        if (locData.place_id) {
          const newUrl = new URL(window.location.href);
          newUrl.searchParams.set('placeId', locData.place_id);
          window.history.pushState({}, '', newUrl.toString());
        }

      } else {
        setError('Keine Locations in der Nähe gefunden.');
      }
    });
  };

  const handleShare = async () => {
    if (!location) return;
    const shareUrl = window.location.href;
    const shareData = {
      title: `Herrenabend bei ${location.name}`,
      text: `Lass uns zu ${location.name} gehen! Adresse: ${location.vicinity}`,
      url: shareUrl,
    };

    if (navigator.share) {
      try {
        await navigator.share(shareData);
      } catch (err) {
        console.error('Share failed:', err);
      }
    } else {
      navigator.clipboard.writeText(shareUrl);
      alert('Link in die Zwischenablage kopiert!');
    }
  };

  const handleOpenMaps = () => {
    if (location?.url) {
      window.open(location.url, '_blank');
    } else if (location?.name) {
      window.open(`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(location.name + ' ' + location.vicinity)}`, '_blank');
    }
  };

  return (
    <div className="w-full flex flex-col items-center gap-6">
      
      {/* Action Button */}
      <button 
        onClick={findNewLocation}
        disabled={loading}
        className="group relative flex items-center justify-center gap-2 w-full sm:w-auto px-8 py-4 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-bold rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 disabled:opacity-70 disabled:cursor-not-allowed transform hover:-translate-y-1"
      >
        <RefreshCw className={`w-5 h-5 ${loading ? 'animate-spin' : 'group-hover:rotate-180 transition-transform duration-500'}`} />
        {loading ? 'Suche läuft...' : 'Location finden'}
      </button>

      {error && (
        <div className="flex items-center gap-2 p-4 bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400 rounded-xl w-full">
          <AlertCircle className="w-5 h-5 shrink-0" />
          <p className="text-sm font-medium">{error}</p>
        </div>
      )}

      {/* Result Card */}
      {location && !loading && (
        <div className="w-full bg-white dark:bg-gray-800 rounded-3xl p-6 shadow-2xl border border-gray-100 dark:border-gray-700 transform transition-all duration-500 animate-in fade-in slide-in-from-bottom-4">
          <div className="flex flex-col gap-4">
            
            <div className="flex justify-between items-start">
              <div>
                <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-1">
                  {location.name}
                </h2>
                <div className="flex items-center gap-1.5 text-gray-500 dark:text-gray-400 text-sm">
                  <MapPin className="w-4 h-4" />
                  <span>{location.vicinity}</span>
                </div>
              </div>
              {location.rating && (
                <div className="flex items-center justify-center bg-yellow-100 dark:bg-yellow-900/30 text-yellow-700 dark:text-yellow-400 font-bold px-3 py-1 rounded-full text-sm">
                  ★ {location.rating}
                </div>
              )}
            </div>

            <div className="grid grid-cols-2 gap-3 mt-4">
              <button 
                onClick={handleOpenMaps}
                className="flex items-center justify-center gap-2 py-3 bg-gray-100 hover:bg-gray-200 dark:bg-gray-700 dark:hover:bg-gray-600 text-gray-900 dark:text-white font-medium rounded-xl transition-colors"
              >
                <Navigation className="w-4 h-4" />
                Route
              </button>
              
              <button 
                onClick={handleShare}
                className="flex items-center justify-center gap-2 py-3 bg-gray-100 hover:bg-gray-200 dark:bg-gray-700 dark:hover:bg-gray-600 text-gray-900 dark:text-white font-medium rounded-xl transition-colors"
              >
                <Share2 className="w-4 h-4" />
                Teilen
              </button>
            </div>

          </div>
        </div>
      )}

    </div>
  );
}
