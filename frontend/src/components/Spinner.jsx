export default function Spinner() {
    return (
        <div className="fixed inset-0 flex items-center justify-center bg-white bg-opacity-70 z-50">
            <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-blue-600 border-solid"></div>
        </div>
    );
}
