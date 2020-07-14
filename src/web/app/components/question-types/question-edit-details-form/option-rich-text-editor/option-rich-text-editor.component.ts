import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

/**
 * A rich text editor.
 */
@Component({
  selector: 'tm-option-rich-text-editor',
  templateUrl: './option-rich-text-editor.component.html',
  styleUrls: ['./option-rich-text-editor.component.scss'],
})
export class OptionRichTextEditorComponent implements OnInit {

  @Input()
  isDisabled: boolean = false;

  @Input()
  isInlineMode: boolean = true;

  @Input()
  minHeightInPx: number = 120;

  @Input()
  placeholderText: string = '';

  @Input()
  richText: string = '';

  @Output()
  richTextChange: EventEmitter<string> = new EventEmitter();

  // the argument passed to tinymce.init() in native JavaScript
  readonly init: any = {
    base_url: '/tinymce',
    skin_url: '/tinymce/skins/ui/oxide',
    suffix: '.min',
    resize: false,
    fontsize_formats: '8pt 9pt 10pt 11pt 12pt 14pt 16pt 18pt 20pt 24pt 26pt 28pt 36pt 48pt 72pt',
    font_formats: 'Andale Mono=andale mono,times;'
        + 'Arial=arial,helvetica,sans-serif;'
        + 'Arial Black=arial black,avant garde;'
        + 'Book Antiqua=book antiqua,palatino;'
        + 'Comic Sans MS=comic sans ms,sans-serif;'
        + 'Courier New=courier new,courier;'
        + 'Georgia=georgia,palatino;'
        + 'Helvetica=helvetica;'
        + 'Impact=impact,chicago;'
        + 'Symbol=symbol;'
        + 'Tahoma=tahoma,arial,helvetica,sans-serif;'
        + 'Terminal=terminal,monaco;'
        + 'Times New Roman=times new roman,times;'
        + 'Trebuchet MS=trebuchet ms,geneva;'
        + 'Verdana=verdana,geneva;'
        + 'Webdings=webdings;'
        + 'Wingdings=wingdings,zapf dingbats',
    relative_urls: false,
    convert_urls: false,
    remove_linebreaks: false,
    plugins: [
      'placeholder',
      'advlist autolink lists link image charmap print hr anchor',
      'searchreplace wordcount visualblocks visualchars code fullscreen',
      'insertdatetime nonbreaking save table directionality',
      'emoticons paste textpattern',
    ],

    menubar: false,
    toolbar1: 'undo redo | bold italic underline | bullist numlist outdent indent | link image table',
    toolbar2: 'forecolor backcolor | fontsizeselect fontselect | charmap emoticons',
  };

  constructor() { }

  ngOnInit(): void {
  }
}
