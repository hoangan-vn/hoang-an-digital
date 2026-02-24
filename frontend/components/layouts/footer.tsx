import Image from 'next/image';
import Link from 'next/link';

export default function Footer() {
  return (
    <footer className='bg-white py-12'>
      <div className='mx-auto max-w-7x sm:px-6 lg:px-8'>
        <div className='grid grid-cols-1 gap-8 md:grid-cols-4'>
          {/* Logo Section */}
          <div className='flex flex-col items-start'>
            <Image
              src='/next.svg' // Assuming you have a lift-logo.svg in your public directory
              alt='Lift Logo'
              width={120}
              height={40}
            />

            <div className='space-y-6 p-4'>
              {/* Address */}
              <div className='flex items-start space-x-3'>
                <MapPin className='mt-1 h-6 w-6 flex-shrink-0 text-blue-600' />
                <p className='text-lg text-gray-700'>
                  Tầng 16, Tòa nhà Ocean Park, Số 01 Đào Duy Anh,
                  <br />
                  Phường Kim Liên, Thành phố Hà Nội.
                </p>
              </div>

              {/* Phone */}
              <div className='flex items-center space-x-3'>
                <Phone className='h-6 w-6 flex-shrink-0 text-blue-600' />
                <a
                  href='tel:+842471089999'
                  className='text-lg text-gray-700 transition-colors hover:text-blue-600'
                >
                  (024) 71 089 999
                </a>
              </div>

              {/* Email */}
              <div className='flex items-center space-x-3'>
                <Mail className='h-6 w-6 flex-shrink-0 text-blue-600' />
                <a
                  href='mailto:info@tenten.vn'
                  className='text-lg text-gray-700 transition-colors hover:text-blue-600'
                >
                  info@tenten.vn
                </a>
              </div>
            </div>
          </div>

          {/* Empresa Section */}
          <div>
            <h3 className='mb-4 text-lg font-semibold text-gray-900'>
              Giới thiệu
            </h3>
            <ul className='space-y-2'>
              <li>
                <Link
                  href='/about'
                  className='text-gray-600 transition-colors hover:text-blue-600'
                >
                  Về chúng tôi
                </Link>
              </li>
              <li>
                <Link
                  href='/solutions'
                  className='text-gray-600 transition-colors hover:text-blue-600'
                >
                  Liên hệ
                </Link>
              </li>
            </ul>
          </div>

          {/* Categorías Section */}
          <div>
            <h3 className='mb-4 text-lg font-semibold text-gray-900'>
              Danh mục
            </h3>
            <ul className='space-y-2'>
              <li>
                <Link
                  href='/hire-talent'
                  className='text-gray-600 transition-colors hover:text-blue-600'
                >
                  Contratar Talento
                </Link>
              </li>
              <li>
                <Link
                  href='/develop-talent'
                  className='text-gray-600 transition-colors hover:text-blue-600'
                >
                  Desarrollar Talento
                </Link>
              </li>
              <li>
                <Link
                  href='/gamification-tools'
                  className='text-gray-600 transition-colors hover:text-blue-600'
                >
                  Herramientas de Gamificación
                </Link>
              </li>
            </ul>
          </div>

          {/* More Categories Section */}
          <div>
            <ul className='space-y-2 pt-10 md:pt-0'>
              {' '}
              {/* Adjusted padding for alignment */}
              <Image
                src='/logo-da-thong-bao-bo-cong-thuong.webp'
                alt='Lift Logo'
                width={120}
                height={40}
              />
              <Image
                src='/logo-da-thong-bao-bo-cong-thuong.webp'
                alt='Lift Logo'
                width={120}
                height={40}
              />
            </ul>
          </div>
        </div>

        <div className='mt-12 flex flex-col items-center justify-between border-t border-gray-200 pt-8 text-sm text-gray-600 sm:flex-row'>
          {/* Social Media Links */}
          <div className='mb-4 flex space-x-4 sm:mb-0'>
            <Link
              href='https://twitter.com'
              target='_blank'
              rel='noopener noreferrer'
              className='transition-colors hover:text-blue-600'
            >
              <Twitter className='h-5 w-5' />
            </Link>
            <Link
              href='https://linkedin.com'
              target='_blank'
              rel='noopener noreferrer'
              className='transition-colors hover:text-blue-600'
            >
              <Linkedin className='h-5 w-5' />
            </Link>
            <Link
              href='https://facebook.com'
              target='_blank'
              rel='noopener noreferrer'
              className='transition-colors hover:text-blue-600'
            >
              <Facebook className='h-5 w-5' />
            </Link>
          </div>

          {/* Legal Links */}
          <div className='flex flex-col space-y-2 sm:flex-row sm:space-y-0 sm:space-x-6'>
            <Link
              href='/privacy-policy'
              className='transition-colors hover:text-blue-600'
            >
              Política de Privacidad
            </Link>
            <Link
              href='/terms-conditions'
              className='transition-colors hover:text-blue-600'
            >
              Términos y Condiciones
            </Link>
            <Link
              href='/code-of-conduct'
              className='transition-colors hover:text-blue-600'
            >
              Código de Conducta
            </Link>
          </div>
        </div>
      </div>
    </footer>
  );
}

// You'll need to install lucide-react for these icons or use your own
// npm install lucide-react
import { Twitter, Linkedin, Facebook, MapPin, Phone, Mail } from 'lucide-react';
