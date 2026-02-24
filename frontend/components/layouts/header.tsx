import Image from 'next/image';
import Link from 'next/link';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Search } from 'lucide-react';
import { useState } from 'react';
import RenderIf from '@/lib/widgets/RenderIf';

export function Header() {
  const [isLogin] = useState<boolean>(false);
  return (
    <nav className='flex w-full items-center justify-between rounded-lg bg-white p-4 shadow-md'>
      {/* Logo */}
      <div className='mr-4 flex-shrink-0'>
        {/* Replace with your actual logo component or image */}
        <div className='relative h-10 w-20'>
          {/* This is a placeholder for the rainbow-like logo. You might replace it with an actual SVG or Image component. */}
          <Image
            src='/next.svg' // Assuming you have a lift-logo.svg in your public directory
            alt='Lift Logo'
            width={120}
            height={40}
          />
        </div>
      </div>

      {/* Search Bar */}
      <div className='mx-4 flex max-w-sm flex-grow items-center rounded-lg bg-blue-50 p-1'>
        <Search className='mx-2 h-5 w-5 text-gray-500' />
        <Input
          type='text'
          placeholder='Nhập tìm kiếm' // Enter search
          className='flex-grow border-none bg-transparent placeholder:text-gray-500 focus-visible:ring-0 focus-visible:ring-offset-0'
        />
        <Button
          variant='ghost'
          className='rounded-md px-4 py-2 text-blue-600 hover:bg-blue-100'
        >
          Tìm kiếm
        </Button>
      </div>

      {/* Navigation Links */}
      <div className='mx-4 hidden items-center space-x-6 text-sm text-gray-700 md:flex'>
        <Link href='#' className='hover:text-blue-600'>
          Dịch vụ
        </Link>
        <Link href='#' className='hover:text-blue-600'>
          Sản phẩm
        </Link>
        <Link href='#' className='hover:text-blue-600'>
          Tin tức
        </Link>
        <Link href='#' className='hover:text-blue-600'>
          Hỗ trợ
        </Link>
        <Link href='#' className='hover:text-blue-600'>
          Giới thiệu
        </Link>
      </div>

      {/* Action Buttons */}
      <div className='ml-4 flex items-center space-x-2'>
        <RenderIf condition={isLogin}>
          <Button
            variant='outline'
            className='border-blue-600 px-4 py-2 text-blue-600 hover:bg-blue-50'
          >
            Profile
          </Button>
        </RenderIf>
        <RenderIf condition={isLogin}>
          <Button
            variant='outline'
            className='border-blue-600 px-4 py-2 text-blue-600 hover:bg-blue-50'
          >
            Login
          </Button>
        </RenderIf>
      </div>
    </nav>
  );
}
