import { useIsMobile } from './use-mobile';

// Standard breakpoints (same as in useIsMobile)
export const BREAKPOINTS = {
  xs: 480, // Extra small devices (phones)
  sm: 640, // Small devices (large phones)
  md: 768, // Medium devices (tablets)
  lg: 1024, // Large devices (laptops)
  xl: 1280, // Extra large devices (desktops)
  '2xl': 1536 // 2X large devices (large desktops)
} as const;

// Hook to check if current width matches a breakpoint
export const useBreakpoint = (breakpoint: keyof typeof BREAKPOINTS) => {
  const { width } = useIsMobile();

  switch (breakpoint) {
    case 'xs':
      return width >= BREAKPOINTS.xs;
    case 'sm':
      return width >= BREAKPOINTS.sm;
    case 'md':
      return width >= BREAKPOINTS.md;
    case 'lg':
      return width >= BREAKPOINTS.lg;
    case 'xl':
      return width >= BREAKPOINTS.xl;
    case '2xl':
      return width >= BREAKPOINTS['2xl'];
    default:
      return false;
  }
};

// Hook to check if current width is between two breakpoints
export const useBreakpointBetween = (
  min: keyof typeof BREAKPOINTS,
  max: keyof typeof BREAKPOINTS
) => {
  const { width } = useIsMobile();

  const minWidth = BREAKPOINTS[min];
  const maxWidth = BREAKPOINTS[max];

  return width >= minWidth && width < maxWidth;
};

// Hook to check if current width is above a breakpoint
export const useBreakpointUp = (breakpoint: keyof typeof BREAKPOINTS) => {
  const { width } = useIsMobile();

  return width >= BREAKPOINTS[breakpoint];
};

// Hook to check if current width is below a breakpoint
export const useBreakpointDown = (breakpoint: keyof typeof BREAKPOINTS) => {
  const { width } = useIsMobile();

  return width < BREAKPOINTS[breakpoint];
};
