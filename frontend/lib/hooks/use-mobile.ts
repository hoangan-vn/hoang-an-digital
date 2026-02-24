'use client';

import { useState, useEffect, useCallback } from 'react';

// Standard breakpoints
const BREAKPOINTS = {
  xs: 480, // Extra small devices (phones)
  sm: 640, // Small devices (large phones)
  md: 768, // Medium devices (tablets)
  lg: 1024, // Large devices (laptops)
  xl: 1280, // Extra large devices (desktops)
  '2xl': 1536 // 2X large devices (large desktops)
} as const;

// Device types
export type DeviceType = 'mobile' | 'tablet' | 'desktop';

// Screen orientation
export type Orientation = 'portrait' | 'landscape';

// Responsive state interface
export interface ResponsiveState {
  isMobile: boolean;
  isTablet: boolean;
  isDesktop: boolean;
  deviceType: DeviceType;
  orientation: Orientation;
  width: number;
  height: number;
  isLandscape: boolean;
  isPortrait: boolean;
  isTouch: boolean;
}

// Debounce function
const debounce = <T extends (...args: unknown[]) => unknown>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = setTimeout(() => func(...args), wait);
  };
};

// Get device type based on width
const getDeviceType = (width: number): DeviceType => {
  if (width < BREAKPOINTS.md) return 'mobile';
  if (width < BREAKPOINTS.lg) return 'tablet';
  return 'desktop';
};

// Get orientation
const getOrientation = (width: number, height: number): Orientation => {
  return width > height ? 'landscape' : 'portrait';
};

// Check if device supports touch
const isTouchDevice = (): boolean => {
  if (typeof window === 'undefined') return false;
  return 'ontouchstart' in window || navigator.maxTouchPoints > 0;
};

export const useIsMobile = (): ResponsiveState => {
  const [state, setState] = useState<ResponsiveState>(() => {
    // SSR fallback
    if (typeof window === 'undefined') {
      return {
        isMobile: false,
        isTablet: false,
        isDesktop: true,
        deviceType: 'desktop',
        orientation: 'portrait',
        width: 1024,
        height: 768,
        isLandscape: false,
        isPortrait: true,
        isTouch: false
      };
    }

    const width = window.innerWidth;
    const height = window.innerHeight;
    const deviceType = getDeviceType(width);
    const orientation = getOrientation(width, height);
    const isTouch = isTouchDevice();

    return {
      isMobile: deviceType === 'mobile',
      isTablet: deviceType === 'tablet',
      isDesktop: deviceType === 'desktop',
      deviceType,
      orientation,
      width,
      height,
      isLandscape: orientation === 'landscape',
      isPortrait: orientation === 'portrait',
      isTouch
    };
  });

  const updateState = useCallback(() => {
    if (typeof window === 'undefined') return;

    const width = window.innerWidth;
    const height = window.innerHeight;
    const deviceType = getDeviceType(width);
    const orientation = getOrientation(width, height);
    const isTouch = isTouchDevice();

    setState({
      isMobile: deviceType === 'mobile',
      isTablet: deviceType === 'tablet',
      isDesktop: deviceType === 'desktop',
      deviceType,
      orientation,
      width,
      height,
      isLandscape: orientation === 'landscape',
      isPortrait: orientation === 'portrait',
      isTouch
    });
  }, []);

  // Debounced update function
  // eslint-disable-next-line react-hooks/exhaustive-deps
  const debouncedUpdate = useCallback(debounce(updateState, 100), [updateState]);

  useEffect(() => {
    if (typeof window === 'undefined') return;

    // Initial check
    updateState();

    // Add event listeners
    window.addEventListener('resize', debouncedUpdate);
    window.addEventListener('orientationchange', debouncedUpdate);

    // Cleanup
    return () => {
      window.removeEventListener('resize', debouncedUpdate);
      window.removeEventListener('orientationchange', debouncedUpdate);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [debouncedUpdate]);

  return state;
};

// Convenience hooks
export const useIsMobileOnly = () => {
  const { isMobile } = useIsMobile();
  return isMobile;
};

export const useIsTablet = () => {
  const { isTablet } = useIsMobile();
  return isTablet;
};

export const useIsDesktop = () => {
  const { isDesktop } = useIsMobile();
  return isDesktop;
};

export const useOrientation = () => {
  const { orientation, isLandscape, isPortrait } = useIsMobile();
  return { orientation, isLandscape, isPortrait };
};

export const useIsTouch = () => {
  const { isTouch } = useIsMobile();
  return isTouch;
};
