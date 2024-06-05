export default function AboutView() {
  return (
    <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
      <h2>Small credit approval app</h2>
      <p className="p-s">Most values can be entirely up to you, but for the personal ID field you can use the following hard coded demo values</p>
        <ul>
            <li>49002010965 - Segment DEBT</li>
            <li>49002010976 - Segment ONE</li>
            <li>49002010987 - Segment TWO</li>
            <li>49002010998 - Segment THREE</li>
        </ul>
    </div>
  );
}
