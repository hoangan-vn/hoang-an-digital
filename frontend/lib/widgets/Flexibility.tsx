import { cn } from '@/lib/utils';

type FlexibilityProps = {
  className?: string;
  children: React.ReactNode;
};

export default function Flexibility({ className, children }: FlexibilityProps) {
  return <div className={cn('flex-1', className)}>{children}</div>;
}
