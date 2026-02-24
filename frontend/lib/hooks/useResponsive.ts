import { useState, useEffect } from 'react';

// Define breakpoint sizes (in pixels)
// sm: ≥ 640px (màn hình nhỏ, như điện thoại landscape)
// md: ≥ 768px (màn hình trung bình, như máy tính bảng)
// lg: ≥ 1024px (màn hình lớn, như laptop)
// xl: ≥ 1280px (màn hình rất lớn, như desktop)
// 2xl: ≥ 1536px (màn hình siêu lớn)
export const breakpoints = {
  mobile: 640,
  tablet: 768,
  laptop: 1024,
  desktop: 1280
};

// Types for responsive states
export type DeviceType = 'mobile' | 'tablet' | 'laptop' | 'desktop';

/**
 * Custom hook that detects the current device type based on screen width
 * @returns Object containing boolean flags for each device type and the current device type
 */
export const useResponsive = () => {
  // Default to the most common device or set undefined if using SSR
  const [deviceType, setDeviceType] = useState<DeviceType | undefined>(undefined);
  const [isMobile, setIsMobile] = useState(false);
  const [isTablet, setIsTablet] = useState(false);
  const [isLaptop, setIsLaptop] = useState(false);
  const [isDesktop, setIsDesktop] = useState(false);

  useEffect(() => {
    // Only run on client side
    if (typeof window === 'undefined') return;

    // Handler to call on window resize
    const handleResize = () => {
      const width = window.innerWidth;
      // Set device type based on breakpoints
      if (width < breakpoints.tablet) {
        setDeviceType('mobile');
        setIsMobile(true);
        setIsTablet(false);
        setIsLaptop(false);
        setIsDesktop(false);
      } else if (width < breakpoints.laptop) {
        setDeviceType('tablet');
        setIsMobile(false);
        setIsTablet(true);
        setIsLaptop(false);
        setIsDesktop(false);
      } else if (width < breakpoints.desktop) {
        setDeviceType('laptop');
        setIsMobile(false);
        setIsTablet(false);
        setIsLaptop(true);
        setIsDesktop(false);
      } else {
        setDeviceType('desktop');
        setIsMobile(false);
        setIsTablet(false);
        setIsLaptop(false);
        setIsDesktop(true);
      }
    };

    // Add event listener
    window.addEventListener('resize', handleResize);

    // Call handler right away so state gets updated with initial window size
    handleResize();

    // Remove event listener on cleanup
    return () => window.removeEventListener('resize', handleResize);
  }, []); // Empty array ensures effect is only run on mount and unmount

  return {
    isMobile,
    isTablet,
    isLaptop,
    isDesktop,
    deviceType
  };
};

/**
 * Custom hook that returns a boolean indicating if the screen width matches the given breakpoint
 * @param breakpoint - The breakpoint to check against
 * @returns Boolean indicating if the screen width is less than or equal to the breakpoint
 */
export const useBreakpoint = (breakpoint: keyof typeof breakpoints) => {
  const [isBelow, setIsBelow] = useState(false);

  useEffect(() => {
    // Only run on client side
    if (typeof window === 'undefined') return;

    const handleResize = () => {
      setIsBelow(window.innerWidth <= breakpoints[breakpoint]);
    };

    window.addEventListener('resize', handleResize);
    handleResize();

    return () => window.removeEventListener('resize', handleResize);
  }, [breakpoint]);

  return isBelow;
};

/**
 * Hook that returns styles based on the current device type
 * @param styles - Object containing styles for each device type
 * @returns The styles for the current device type
 */
export const useResponsiveStyles = <T>(styles: { mobile?: T; tablet?: T; laptop?: T; desktop?: T; default: T }): T => {
  const { deviceType } = useResponsive();

  if (!deviceType) {
    return styles.default;
  }

  return styles[deviceType] || styles.default;
};
